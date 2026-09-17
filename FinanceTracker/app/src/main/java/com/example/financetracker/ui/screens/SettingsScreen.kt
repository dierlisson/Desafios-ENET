package com.example.financetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.ui.components.GlassCard
import com.example.financetracker.ui.theme.DarkBackground
import com.example.financetracker.ui.theme.DarkCardSurface
import com.example.financetracker.ui.theme.DarkSurface
import com.example.financetracker.ui.theme.GlassBorder
import com.example.financetracker.ui.theme.NeonCyan
import com.example.financetracker.ui.theme.NeonMint
import com.example.financetracker.ui.theme.NeonPurple
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun SettingsScreen() {
    var isAudioEnabled by remember { mutableStateOf(true) }
    var isParticlesEnabled by remember { mutableStateOf(true) }
    var particleCount by remember { mutableFloatStateOf(20f) }

    var isBiometricEnabled by remember { mutableStateOf(true) }
    var isHideBalanceEnabled by remember { mutableStateOf(false) }
    var isDriveBackupEnabled by remember { mutableStateOf(true) }

    var simulationMessage by remember { mutableStateOf<String?>(null) }

    val particlePresets = listOf("Econômico (6)", "Sutil (10)", "Equilibrado (20)", "Hyperdrive (36)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Avatar & System Status
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(NeonPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Configurações do Sistema", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Terminal Pro • Ativo", color = NeonMint, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonMint.copy(alpha = 0.2f))
                        .border(1.dp, NeonMint, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("120Hz ONLINE", color = NeonMint, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Seção 1: Áudio & Sinais Acústicos
            item {
                Text("🔊 ÁUDIO & SINAIS ACÚSTICOS", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Som ao Desfazer Exclusão", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Toca um bi-tom ao restaurar transações", color = TextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = isAudioEnabled,
                                onCheckedChange = { isAudioEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = TextPrimary,
                                    checkedTrackColor = NeonMint
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                simulationMessage = "🔊 Tocando bi-tom + haptic test..."
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple.copy(alpha = 0.4f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("[ Ouvir Bi-tom + Haptic ]", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Seção 2: Efeitos Visuais & Telemetria Neon
            item {
                Text("✨ EFEITOS VISUAIS & TELEMETRIA NEON", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Partículas Neon na Restauração", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Explosão de partículas no undo action", color = TextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = isParticlesEnabled,
                                onCheckedChange = { isParticlesEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = TextPrimary,
                                    checkedTrackColor = NeonPurple
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Densidade de Partículas: ${particleCount.toInt()}", color = TextSecondary, fontSize = 12.sp)
                        Slider(
                            value = particleCount,
                            onValueChange = { particleCount = it },
                            valueRange = 4f..50f,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonMint,
                                activeTrackColor = NeonMint,
                                inactiveTrackColor = DarkCardSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(particlePresets) { preset ->
                                val count = preset.substringAfter("(").substringBefore(")").toFloatOrNull() ?: 20f
                                val isSelected = particleCount.toInt() == count.toInt()
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) NeonMint else DarkCardSurface)
                                        .clickable { particleCount = count }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = preset,
                                        color = if (isSelected) DarkBackground else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Card de Teste em Tempo Real
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurface)
                                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Simular Exclusão & Restauração", color = TextPrimary, fontSize = 12.sp)
                                Button(
                                    onClick = {
                                        simulationMessage = "💥 Explosão de ${particleCount.toInt()} partículas Neon disparada!"
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Simular", color = DarkBackground, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Seção 3: Preferências & Segurança
            item {
                Text("🔒 PREFERÊNCIAS & SEGURANÇA", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SettingToggleRow("Biometria ao abrir (Fingerprint / Face ID)", isBiometricEnabled) { isBiometricEnabled = it }
                        Spacer(modifier = Modifier.height(10.dp))
                        SettingToggleRow("Ocultar saldos por padrão", isHideBalanceEnabled) { isHideBalanceEnabled = it }
                        Spacer(modifier = Modifier.height(10.dp))
                        SettingToggleRow("Backup Automático via Drive", isDriveBackupEnabled) { isDriveBackupEnabled = it }
                    }
                }
            }

            if (simulationMessage != null) {
                item {
                    Text(simulationMessage!!, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SettingToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextPrimary, fontSize = 13.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextPrimary,
                checkedTrackColor = NeonPurple
            )
        )
    }
}
