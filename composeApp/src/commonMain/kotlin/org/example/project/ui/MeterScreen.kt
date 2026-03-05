package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.viewmodel.MeterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterScreen(viewModel: MeterViewModel) {
    var service by remember { mutableStateOf("") }
    var reading by remember { mutableStateOf("") }

    val primaryNeon = Color(0xFF818CF8)
    val darkBackground = Color(0xFF0F172A)
    val cardBackground = Color(0xFF1E293B)
    val successGreen = Color(0xFF10B981)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("SMART METER", fontWeight = FontWeight.ExtraBold, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = darkBackground)
            )
        },
        containerColor = darkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("CALCULATE CONSUMPTION", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = primaryNeon)
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = service,
                            onValueChange = { if (it.length <= 10) service = it },
                            label = { Text("Service Number") },
                            leadingIcon = { Icon(Icons.Default.AccountBox, null, tint = primaryNeon) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = reading,
                            onValueChange = { if (it.all { c -> c.isDigit() }) reading = it },
                            label = { Text("Current Reading") },
                            leadingIcon = { Icon(Icons.Default.Speed, null, tint = primaryNeon) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.submit(service, reading.toIntOrNull() ?: 0) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryNeon)
                        ) {
                            Text("SUBMIT", color = darkBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                viewModel.error?.let { msg ->
                    Text(msg, color = Color.Red, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                }

                viewModel.cost?.let { bill ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = successGreen)
                    ) {
                        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("TOTAL COST", color = darkBackground.copy(0.7f), fontWeight = FontWeight.Bold)
                                Text("$${bill}", color = darkBackground, fontSize = 32.sp, fontWeight = FontWeight.Black)
                            }
                            FloatingActionButton(
                                onClick = {
                                    viewModel.save(service, reading.toIntOrNull() ?: 0)
                                    // Keep 'service' filled if you want to see filtered results
                                    reading = ""
                                },
                                containerColor = darkBackground
                            ) { Icon(Icons.Default.Save, null, tint = Color.White) }
                        }
                    }
                }
            }

            item {
                Text("RECENT ACTIVITY", fontWeight = FontWeight.Bold, color = Color.White)
            }

            // Removed the filter so you can always see that it's saving correctly
            items(viewModel.history.take(5)) { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = cardBackground
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, null, tint = primaryNeon)
                        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                            Text("Reading: ${item.reading}", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("ID: ${item.serviceNumber}", color = Color.Gray, fontSize = 11.sp)
                        }
                        Text("$${item.cost}", color = successGreen, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}