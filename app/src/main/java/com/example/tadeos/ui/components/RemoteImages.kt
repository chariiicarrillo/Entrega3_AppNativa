package com.example.tadeos.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import coil3.compose.AsyncImage
import com.example.tadeos.R
import com.example.tadeos.data.model.Pet

@Composable
fun TadeosPetImage(
    pet: Pet,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val fallback = painterResource(id = pet.localPhotoRes())

    if (pet.photoUrl.isNotBlank()) {
        AsyncImage(
            model = pet.photoUrl,
            contentDescription = "Foto de ${pet.name}",
            placeholder = fallback,
            error = fallback,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Image(
            painter = fallback,
            contentDescription = "Foto de ${pet.name}",
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

@Composable
fun TadeosProfileImage(
    photoUrl: String,
    localImageUri: Uri?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val model = localImageUri ?: photoUrl.takeIf { it.isNotBlank() }

    if (model != null) {
        AsyncImage(
            model = model,
            contentDescription = "Foto de perfil",
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Avatar",
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

private fun Pet.localPhotoRes(): Int {
    val key = photoKey.ifBlank { name }.lowercase()

    return when {
        key.contains("luna") -> R.drawable.pet_luna
        key.contains("cooper") -> R.drawable.pet_cooper
        key.contains("otto") -> R.drawable.pet_otto
        else -> R.drawable.logo_tadeos
    }
}
