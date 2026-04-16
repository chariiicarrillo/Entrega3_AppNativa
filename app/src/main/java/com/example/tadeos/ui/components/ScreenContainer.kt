package com.example.tadeos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tadeos.navigation.AppRoutes

@Composable
fun ScreenContainer(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    selectedRoute: String? = null,
    onHomeClick: () -> Unit = {},
    onPetsClick: () -> Unit = {},
    onHealthClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (selectedRoute != null) {
                TadeosBottomBar(
                    selectedRoute = selectedRoute,
                    onHomeClick = onHomeClick,
                    onPetsClick = onPetsClick,
                    onHealthClick = onHealthClick,
                    onProfileClick = onProfileClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Tadeo's",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            content()
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun TadeosCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
fun PrimaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun SecondaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ActionGrid(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun TadeosBottomBar(
    selectedRoute: String,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute == AppRoutes.Home.route,
            onClick = onHomeClick,
            icon = { Text(text = "I") },
            label = { Text(text = "Inicio") }
        )
        NavigationBarItem(
            selected = selectedRoute == AppRoutes.PetsList.route,
            onClick = onPetsClick,
            icon = { Text(text = "M") },
            label = { Text(text = "Mascotas") }
        )
        NavigationBarItem(
            selected = selectedRoute == AppRoutes.Health.route,
            onClick = onHealthClick,
            icon = { Text(text = "S") },
            label = { Text(text = "Salud") }
        )
        NavigationBarItem(
            selected = selectedRoute == AppRoutes.Profile.route,
            onClick = onProfileClick,
            icon = { Text(text = "P") },
            label = { Text(text = "Perfil") }
        )
    }
}
