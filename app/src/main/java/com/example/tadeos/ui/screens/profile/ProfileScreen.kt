package com.example.tadeos.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SectionTitle
import com.example.tadeos.ui.components.TadeosBrandHeader
import com.example.tadeos.ui.components.TadeosCard
import com.example.tadeos.ui.components.TadeosProfileImage
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.TerracottaClay

private val ProfileBackground = Color(0xFFFCF7EF)

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    var isDirty by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedPhotoUri = uri
        if (uri != null) {
            isDirty = true
        }
    }

    DisposableEffect(Unit) {
        val listener = TadeosFirebaseRepository.observeUserProfile { profile, message ->
            statusMessage = message
            if (profile != null && !isDirty) {
                name = profile.name
                email = profile.email
                phone = profile.phone
                photoUrl = profile.photoUrl
            }
        }

        onDispose {
            listener?.remove()
        }
    }

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.Profile.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = {},
        containerColor = ProfileBackground,
        horizontalPadding = 18,
        verticalPadding = 16
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TadeosBrandHeader(
                onBrandClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Editar Perfil",
                    color = TerracottaClay,
                    fontSize = 29.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Configuracion de tu cuenta personal.",
                    modifier = Modifier.padding(top = 2.dp),
                    color = MutedBrown,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                TadeosProfileImage(
                    photoUrl = photoUrl,
                    localImageUri = selectedPhotoUri,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .clickable {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .align(Alignment.BottomCenter)
                        .clickable {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Cambiar foto",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name.ifBlank { "Usuario" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Usuario verificado",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            TadeosCard {
                ProfileField(
                    label = "Nombre",
                    value = name,
                    onValueChange = {
                        name = it
                        isDirty = true
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Nombre",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                ProfileField(
                    label = "Correo",
                    value = email,
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Correo",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                ProfileField(
                    label = "Telefono",
                    value = phone,
                    onValueChange = {
                        phone = it
                        isDirty = true
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "Telefono",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            if (statusMessage != null) {
                Text(
                    text = statusMessage.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    color = if (statusMessage == "Perfil actualizado.") {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    val cleanName = name.trim()
                    if (cleanName.isBlank()) {
                        statusMessage = "Ingresa tu nombre."
                        return@Button
                    }

                    isSaving = true
                    statusMessage = null

                    TadeosFirebaseRepository.updateUserProfile(
                        name = cleanName,
                        phone = phone.trim(),
                        imageUri = selectedPhotoUri
                    ) { success, message ->
                        isSaving = false
                        if (success) {
                            isDirty = false
                            selectedPhotoUri = null
                            statusMessage = "Perfil actualizado."
                        } else {
                            statusMessage = message
                        }
                    }
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isSaving) "Guardando..." else "Guardar cambios")
            }

            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cerrar sesion")
            }

            SectionTitle(text = "Preferencias")

            PreferenceCard(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Privacidad",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                title = "Privacidad",
                description = "Gestiona tu visibilidad y acceso a datos."
            )

            PreferenceCard(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                title = "Notificaciones",
                description = "Configura alertas de vacunas, examenes y medicamentos."
            )
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    readOnly: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            singleLine = true,
            readOnly = readOnly,
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
private fun PreferenceCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String
) {
    TadeosCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            icon()
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
