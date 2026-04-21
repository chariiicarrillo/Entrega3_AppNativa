package com.tadeos.app.data.repository

import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.tadeos.app.data.model.HealthRecord
import com.tadeos.app.data.model.HealthRecordTypes
import com.tadeos.app.data.model.Pet
import com.tadeos.app.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata

object TadeosFirebaseRepository {
    private const val FIREBASE_TIMEOUT_MS = 20000L

    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    private val storage: FirebaseStorage
        get() = FirebaseStorage.getInstance().apply {
            maxUploadRetryTimeMillis = FIREBASE_TIMEOUT_MS
            maxOperationRetryTimeMillis = FIREBASE_TIMEOUT_MS
        }

    private fun usersCollection() = firestore.collection("users")

    private fun userDocument(uid: String) = usersCollection().document(uid)

    private fun petsCollection(uid: String) = userDocument(uid).collection("pets")

    private fun healthRecordsCollection(uid: String) = userDocument(uid).collection("healthRecords")

    fun currentUserId(): String? = auth.currentUser?.uid

    fun saveRegisteredUserProfile(
        uid: String,
        name: String,
        email: String,
        phone: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val userData = hashMapOf<String, Any>(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "phone" to phone,
            "photoUrl" to "",
            "photoStoragePath" to "",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        userDocument(uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { exception ->
                onComplete(false, friendlyFirebaseMessage(exception))
            }
    }

    fun saveCurrentProviderProfile(
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onComplete(false, "No pudimos obtener el usuario autenticado.")
            return
        }

        val userData = mutableMapOf<String, Any>(
            "uid" to user.uid,
            "email" to user.email.orEmpty(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        if (!user.displayName.isNullOrBlank()) {
            userData["name"] = user.displayName.orEmpty()
        }

        user.photoUrl?.let { photoUri ->
            userData["photoUrl"] = photoUri.toString()
        }

        userDocument(user.uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { exception ->
                onComplete(false, friendlyFirebaseMessage(exception))
            }
    }

    fun observeUserProfile(
        onChange: (UserProfile?, String?) -> Unit
    ): ListenerRegistration? {
        val user = auth.currentUser
        if (user == null) {
            onChange(null, "Inicia sesion para cargar tu perfil.")
            return null
        }

        return userDocument(user.uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                onChange(defaultProfile(), friendlyFirebaseMessage(error))
                return@addSnapshotListener
            }

            onChange(snapshot?.toUserProfile() ?: defaultProfile(), null)
        }
    }

    fun updateUserProfile(
        name: String,
        phone: String,
        imageUri: Uri?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onComplete(false, "Inicia sesion para actualizar tu perfil.")
            return
        }

        fun saveProfile(photoUrl: String? = null, photoStoragePath: String? = null) {
            var localListener: ListenerRegistration? = null
            val complete = guardedBooleanCompletion(
                timeoutMessage = "Firestore tarda demasiado en guardar el perfil. Verifica que Cloud Firestore este activo.",
                onComplete = onComplete,
                onFinish = {
                    localListener?.remove()
                }
            )
            val data = mutableMapOf<String, Any>(
                "uid" to user.uid,
                "name" to name,
                "email" to user.email.orEmpty(),
                "phone" to phone,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            if (photoUrl != null && photoStoragePath != null) {
                data["photoUrl"] = photoUrl
                data["photoStoragePath"] = photoStoragePath
            }

            localListener = userDocument(user.uid).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    complete(false, friendlyFirebaseMessage(error))
                    return@addSnapshotListener
                }

                if (snapshot?.exists() == true && snapshot.matchesProfileUpdate(name, phone, photoUrl)) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .apply {
                            if (photoUrl != null) {
                                setPhotoUri(Uri.parse(photoUrl))
                            }
                        }
                        .build()

                    user.updateProfile(profileUpdates)
                    complete(true, null)
                }
            }

            userDocument(user.uid)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .apply {
                            if (photoUrl != null) {
                                setPhotoUri(Uri.parse(photoUrl))
                            }
                        }
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { complete(true, null) }
                }
                .addOnFailureListener { exception ->
                    complete(false, friendlyFirebaseMessage(exception))
                }
        }

        if (imageUri == null) {
            saveProfile()
        } else {
            uploadImage(
                imageUri = imageUri,
                storagePath = "users/${user.uid}/profile/profile.jpg"
            ) { photoUrl, photoPath, errorMessage ->
                if (errorMessage != null || photoUrl == null || photoPath == null) {
                    onComplete(false, errorMessage ?: "No pudimos subir la foto de perfil.")
                } else {
                    saveProfile(photoUrl = photoUrl, photoStoragePath = photoPath)
                }
            }
        }
    }

    fun observePets(
        onChange: (List<Pet>, String?) -> Unit
    ): ListenerRegistration? {
        val uid = currentUserId()
        if (uid == null) {
            onChange(emptyList(), "Inicia sesion para cargar tus mascotas.")
            return null
        }

        return petsCollection(uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(emptyList(), friendlyFirebaseMessage(error))
                    return@addSnapshotListener
                }

                val pets = snapshot?.documents
                    ?.mapNotNull { document -> document.toPet(uid) }
                    .orEmpty()

                onChange(pets, null)
            }
    }

    fun observePet(
        petId: String,
        onChange: (Pet?, String?) -> Unit
    ): ListenerRegistration? {
        val uid = currentUserId()
        if (uid == null) {
            onChange(null, "Inicia sesion para cargar esta mascota.")
            return null
        }

        return petsCollection(uid).document(petId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(null, friendlyFirebaseMessage(error))
                    return@addSnapshotListener
                }

                onChange(snapshot?.toPet(uid), null)
            }
    }

    fun observeHealthRecords(
        petId: String,
        onChange: (List<HealthRecord>, String?) -> Unit
    ): ListenerRegistration? {
        val uid = currentUserId()
        if (uid == null) {
            onChange(emptyList(), "Inicia sesion para cargar el historial de salud.")
            return null
        }

        return healthRecordsCollection(uid)
            .whereEqualTo("petId", petId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(emptyList(), friendlyFirebaseMessage(error))
                    return@addSnapshotListener
                }

                val records = snapshot?.documents
                    ?.mapNotNull { document -> document.toHealthRecord(uid) }
                    ?.sortedByDescending { record -> record.dateMillis }
                    .orEmpty()

                onChange(records, null)
            }
    }

    fun observeHealthRecord(
        recordId: String,
        onChange: (HealthRecord?, String?) -> Unit
    ): ListenerRegistration? {
        val uid = currentUserId()
        if (uid == null) {
            onChange(null, "Inicia sesion para cargar este registro.")
            return null
        }

        return healthRecordsCollection(uid).document(recordId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(null, friendlyFirebaseMessage(error))
                    return@addSnapshotListener
                }

                onChange(snapshot?.toHealthRecord(uid), null)
            }
    }

    fun createHealthRecord(
        record: HealthRecord,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val uid = currentUserId()
        if (uid == null) {
            onComplete(false, "Inicia sesion para guardar el registro de salud.")
            return
        }
        if (record.petId.isBlank()) {
            onComplete(false, "Selecciona una mascota para guardar el registro.")
            return
        }

        val document = healthRecordsCollection(uid).document()
        val recordToSave = record.copy(
            id = document.id,
            userId = uid
        )
        val data = recordToSave.toFirestoreMap().toMutableMap()
        data["createdAt"] = FieldValue.serverTimestamp()
        data["updatedAt"] = FieldValue.serverTimestamp()

        document
            .set(data)
            .addOnSuccessListener {
                updatePetHealthSummary(recordToSave)
                onComplete(true, null)
            }
            .addOnFailureListener { exception ->
                onComplete(false, friendlyFirebaseMessage(exception))
            }
    }

    private fun updatePetHealthSummary(record: HealthRecord) {
        val uid = currentUserId() ?: return
        val summary = mutableMapOf<String, Any>(
            "updatedAt" to FieldValue.serverTimestamp()
        )

        when (record.type) {
            HealthRecordTypes.VACCINE -> {
                summary["vaccines"] = record.title
                summary["healthStatus"] = record.title
            }
            HealthRecordTypes.DEWORMER -> {
                summary["nextCare"] = record.title
            }
            HealthRecordTypes.EXAM -> {
                summary["nextExam"] = record.title
                summary["lastVisit"] = record.displayDate()
            }
            HealthRecordTypes.DIET -> {
                summary["diet"] = record.title.ifBlank { record.subtitle }
            }
            HealthRecordTypes.MOOD -> {
                summary["mood"] = record.title
                summary["recentNote"] = record.notes.ifBlank { "Estado de animo actualizado." }
            }
            HealthRecordTypes.MEDICATION -> {
                summary["healthStatus"] = record.title
            }
        }

        petsCollection(uid).document(record.petId).set(summary, SetOptions.merge())
    }

    fun uploadProfileImage(
        imageUri: Uri,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onComplete(false, "Inicia sesion para actualizar tu foto.")
            return
        }

        uploadImage(
            imageUri = imageUri,
            storagePath = "users/${user.uid}/profile/profile.jpg"
        ) { photoUrl, photoPath, errorMessage ->
            if (errorMessage != null || photoUrl == null || photoPath == null) {
                onComplete(false, errorMessage ?: "No pudimos subir la foto de perfil.")
                return@uploadImage
            }

            userDocument(user.uid)
                .set(
                    mapOf(
                        "photoUrl" to photoUrl,
                        "photoStoragePath" to photoPath,
                        "updatedAt" to FieldValue.serverTimestamp()
                    ),
                    SetOptions.merge()
                )
                .addOnSuccessListener {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(photoUrl))
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { onComplete(true, null) }
                }
                .addOnFailureListener { exception ->
                    onComplete(false, friendlyFirebaseMessage(exception))
                }
        }
    }

    fun uploadPetImage(
        petId: String,
        imageUri: Uri,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val uid = currentUserId()
        if (uid == null || petId.isBlank()) {
            onComplete(false, "No pudimos identificar la mascota.")
            return
        }

        uploadImage(
            imageUri = imageUri,
            storagePath = "users/$uid/pets/$petId/profile.jpg"
        ) { photoUrl, photoPath, errorMessage ->
            if (errorMessage != null || photoUrl == null || photoPath == null) {
                onComplete(false, errorMessage ?: "No pudimos subir la foto de la mascota.")
                return@uploadImage
            }

            petsCollection(uid).document(petId)
                .set(
                    mapOf(
                        "photoUrl" to photoUrl,
                        "photoStoragePath" to photoPath,
                        "updatedAt" to FieldValue.serverTimestamp()
                    ),
                    SetOptions.merge()
                )
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { exception ->
                    onComplete(false, friendlyFirebaseMessage(exception))
                }
        }
    }

    fun createPet(
        pet: Pet,
        imageUri: Uri?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val uid = currentUserId()
        if (uid == null) {
            onComplete(false, "Inicia sesion para guardar la mascota.")
            return
        }

        val document = petsCollection(uid).document()
        val basePet = pet.copy(
            id = document.id,
            userId = uid
        )

        fun savePet(photoUrl: String = "", photoStoragePath: String = "") {
            var localListener: ListenerRegistration? = null
            val complete = guardedBooleanCompletion(
                timeoutMessage = "Firestore tarda demasiado en guardar la mascota. Verifica que Cloud Firestore este activo.",
                onComplete = onComplete,
                onFinish = {
                    localListener?.remove()
                }
            )
            val petToSave = basePet.copy(
                photoUrl = photoUrl,
                photoStoragePath = photoStoragePath
            )

            val data = petToSave.toFirestoreMap().toMutableMap()
            data["createdAt"] = FieldValue.serverTimestamp()
            data["updatedAt"] = FieldValue.serverTimestamp()

            localListener = document.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    complete(false, friendlyFirebaseMessage(error))
                    return@addSnapshotListener
                }

                if (snapshot?.exists() == true) {
                    complete(true, null)
                }
            }

            document
                .set(data)
                .addOnSuccessListener { complete(true, null) }
                .addOnFailureListener { exception ->
                    complete(false, friendlyFirebaseMessage(exception))
                }
        }

        if (imageUri == null) {
            savePet()
        } else {
            uploadImage(
                imageUri = imageUri,
                storagePath = "users/$uid/pets/${document.id}/profile.jpg"
            ) { photoUrl, photoPath, errorMessage ->
                if (errorMessage != null || photoUrl == null || photoPath == null) {
                    onComplete(false, errorMessage ?: "No pudimos subir la foto de la mascota.")
                } else {
                    savePet(photoUrl = photoUrl, photoStoragePath = photoPath)
                }
            }
        }
    }

    fun updatePet(
        pet: Pet,
        imageUri: Uri?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val uid = currentUserId()
        if (uid == null || pet.id.isBlank()) {
            onComplete(false, "No pudimos identificar la mascota.")
            return
        }

        fun savePet(photoUrl: String = pet.photoUrl, photoStoragePath: String = pet.photoStoragePath) {
            val data = pet.copy(
                userId = uid,
                photoUrl = photoUrl,
                photoStoragePath = photoStoragePath
            ).toFirestoreMap().toMutableMap()
            data["updatedAt"] = FieldValue.serverTimestamp()

            petsCollection(uid).document(pet.id)
                .set(data, SetOptions.merge())
                .addOnSuccessListener { onComplete(true, null) }
                .addOnFailureListener { exception ->
                    onComplete(false, friendlyFirebaseMessage(exception))
                }
        }

        if (imageUri == null) {
            savePet()
        } else {
            uploadImage(
                imageUri = imageUri,
                storagePath = "users/$uid/pets/${pet.id}/profile.jpg"
            ) { photoUrl, photoPath, errorMessage ->
                if (errorMessage != null || photoUrl == null || photoPath == null) {
                    onComplete(false, errorMessage ?: "No pudimos subir la foto de la mascota.")
                } else {
                    savePet(photoUrl = photoUrl, photoStoragePath = photoPath)
                }
            }
        }
    }

    fun deletePet(
        pet: Pet,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val uid = currentUserId()
        if (uid == null || pet.id.isBlank()) {
            onComplete(false, "No pudimos identificar la mascota.")
            return
        }

        petsCollection(uid).document(pet.id)
            .delete()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { exception ->
                onComplete(false, friendlyFirebaseMessage(exception))
        }
    }

    private fun guardedBooleanCompletion(
        timeoutMessage: String,
        onComplete: (Boolean, String?) -> Unit,
        onFinish: () -> Unit = {}
    ): (Boolean, String?) -> Unit {
        var didComplete = false
        val timeoutHandler = Handler(Looper.getMainLooper())
        lateinit var complete: (Boolean, String?) -> Unit

        val timeoutRunnable = Runnable {
            complete(false, timeoutMessage)
        }

        complete = { success, message ->
            if (!didComplete) {
                didComplete = true
                timeoutHandler.removeCallbacks(timeoutRunnable)
                onFinish()
                onComplete(success, message)
            }
        }

        timeoutHandler.postDelayed(timeoutRunnable, FIREBASE_TIMEOUT_MS)
        return complete
    }

    private fun uploadImage(
        imageUri: Uri,
        storagePath: String,
        onComplete: (String?, String?, String?) -> Unit
    ) {
        var didComplete = false
        val timeoutHandler = Handler(Looper.getMainLooper())
        val timeoutRunnable = Runnable {
            if (!didComplete) {
                didComplete = true
                onComplete(
                    null,
                    null,
                    "La imagen tarda demasiado en subir. Revisa Storage, las reglas o la conexion e intenta de nuevo."
                )
            }
        }
        val imageReference = storage.reference.child(storagePath)
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

        timeoutHandler.postDelayed(timeoutRunnable, FIREBASE_TIMEOUT_MS)

        imageReference.putFile(imageUri, metadata)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { exception -> throw exception }
                }

                imageReference.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                if (!didComplete) {
                    didComplete = true
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    onComplete(downloadUri.toString(), storagePath, null)
                }
            }
            .addOnFailureListener { exception ->
                if (!didComplete) {
                    didComplete = true
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    onComplete(null, null, friendlyFirebaseMessage(exception))
                }
            }
            .addOnCanceledListener {
                if (!didComplete) {
                    didComplete = true
                    timeoutHandler.removeCallbacks(timeoutRunnable)
                    onComplete(null, null, "Se cancelo la subida de la imagen.")
                }
            }
    }

    private fun defaultProfile(): UserProfile {
        val user = auth.currentUser
        return UserProfile(
            uid = user?.uid.orEmpty(),
            name = user?.displayName.orEmpty(),
            email = user?.email.orEmpty(),
            phone = "",
            photoUrl = user?.photoUrl?.toString().orEmpty()
        )
    }

    private fun DocumentSnapshot.toUserProfile(): UserProfile {
        val fallback = defaultProfile()

        return UserProfile(
            uid = getString("uid").orEmpty().ifBlank { id },
            name = getString("name").orEmpty().ifBlank { fallback.name },
            email = getString("email").orEmpty().ifBlank { fallback.email },
            phone = getString("phone").orEmpty(),
            photoUrl = getString("photoUrl").orEmpty().ifBlank { fallback.photoUrl },
            photoStoragePath = getString("photoStoragePath").orEmpty()
        )
    }

    private fun DocumentSnapshot.matchesProfileUpdate(
        name: String,
        phone: String,
        photoUrl: String?
    ): Boolean {
        val hasBaseData = getString("name").orEmpty() == name &&
            getString("phone").orEmpty() == phone

        val hasPhotoData = photoUrl == null || getString("photoUrl").orEmpty() == photoUrl

        return hasBaseData && hasPhotoData
    }

    private fun DocumentSnapshot.toPet(uid: String): Pet? {
        val petName = getString("name").orEmpty()
        if (petName.isBlank()) {
            return null
        }

        return Pet(
            id = getString("id").orEmpty().ifBlank { id },
            userId = getString("userId").orEmpty().ifBlank { uid },
            name = petName,
            species = getString("species").orEmpty(),
            breed = getString("breed").orEmpty(),
            age = getString("age").orEmpty(),
            weight = getString("weight").orEmpty(),
            healthStatus = getString("healthStatus").orEmpty(),
            nextCare = getString("nextCare").orEmpty(),
            photoUrl = getString("photoUrl").orEmpty(),
            photoStoragePath = getString("photoStoragePath").orEmpty(),
            photoKey = getString("photoKey").orEmpty(),
            gender = getString("gender").orEmpty().ifBlank { "Macho" },
            birthday = getString("birthday").orEmpty(),
            lastVisit = getString("lastVisit").orEmpty().ifBlank { "Sin visitas" },
            mood = getString("mood").orEmpty().ifBlank { "Activo" },
            diet = getString("diet").orEmpty().ifBlank { "Sin definir" },
            vaccines = getString("vaccines").orEmpty().ifBlank { "Pendiente" },
            nextExam = getString("nextExam").orEmpty().ifBlank { "Por programar" },
            microchipId = getString("microchipId").orEmpty().ifBlank { "Sin registrar" },
            coatColor = getString("coatColor").orEmpty().ifBlank { "Sin registrar" },
            recentNote = getString("recentNote").orEmpty().ifBlank { "Sin notas recientes." },
            favorite = getBoolean("favorite") ?: false
        )
    }

    private fun Pet.toFirestoreMap(): Map<String, Any> {
        return hashMapOf(
            "id" to id,
            "userId" to userId,
            "name" to name,
            "species" to species,
            "breed" to breed,
            "age" to age,
            "weight" to weight,
            "healthStatus" to healthStatus,
            "nextCare" to nextCare,
            "photoUrl" to photoUrl,
            "photoStoragePath" to photoStoragePath,
            "photoKey" to photoKey,
            "gender" to gender,
            "birthday" to birthday,
            "lastVisit" to lastVisit,
            "mood" to mood,
            "diet" to diet,
            "vaccines" to vaccines,
            "nextExam" to nextExam,
            "microchipId" to microchipId,
            "coatColor" to coatColor,
            "recentNote" to recentNote,
            "favorite" to favorite
        )
    }

    private fun DocumentSnapshot.toHealthRecord(uid: String): HealthRecord? {
        val title = getString("title").orEmpty()
        val petId = getString("petId").orEmpty()
        if (title.isBlank() || petId.isBlank()) {
            return null
        }

        return HealthRecord(
            id = getString("id").orEmpty().ifBlank { id },
            petId = petId,
            userId = getString("userId").orEmpty().ifBlank { uid },
            type = getString("type").orEmpty(),
            title = title,
            subtitle = getString("subtitle").orEmpty(),
            dateMillis = getLong("dateMillis") ?: 0L,
            time = getString("time").orEmpty(),
            clinic = getString("clinic").orEmpty(),
            vet = getString("vet").orEmpty(),
            notes = getString("notes").orEmpty()
        )
    }

    private fun HealthRecord.toFirestoreMap(): Map<String, Any> {
        return hashMapOf(
            "id" to id,
            "petId" to petId,
            "userId" to userId,
            "type" to type,
            "title" to title,
            "subtitle" to subtitle,
            "dateMillis" to dateMillis,
            "time" to time,
            "clinic" to clinic,
            "vet" to vet,
            "notes" to notes
        )
    }

    private fun HealthRecord.displayDate(): String {
        return if (dateMillis > 0L) {
            java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(dateMillis))
        } else {
            "Sin fecha"
        }
    }

    private fun friendlyFirebaseMessage(exception: Exception): String {
        val rawMessage = exception.localizedMessage.orEmpty()

        return when {
            rawMessage.contains("permission", ignoreCase = true) -> {
                "Firebase no permite esta accion. Revisa las reglas de Firestore o Storage."
            }
            rawMessage.contains("Cloud Firestore API", ignoreCase = true) ||
                rawMessage.contains("firestore.googleapis.com", ignoreCase = true) -> {
                "Activa Cloud Firestore en Firebase Console y espera unos minutos antes de intentar de nuevo."
            }
            rawMessage.contains("network", ignoreCase = true) -> {
                "Revisa tu conexion a internet e intenta de nuevo."
            }
            rawMessage.contains("bucket", ignoreCase = true) -> {
                "Activa Firebase Storage y verifica el bucket del proyecto."
            }
            rawMessage.contains("Object does not exist", ignoreCase = true) -> {
                "No pudimos guardar la imagen en Storage. Verifica que Firebase Storage este activo e intenta de nuevo."
            }
            rawMessage.isNotBlank() -> rawMessage
            else -> "No pudimos completar la accion en Firebase."
        }
    }
}
