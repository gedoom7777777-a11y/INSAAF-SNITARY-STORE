package com.insaaf.sanitarystore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF123B32)
private data class Product(val name: String, val category: String, val price: Int)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StoreApp() }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreApp() {
    val products = listOf(
        Product("PVC Pipe", "Pipes", 450), Product("PPRC Pipe", "Pipes", 780),
        Product("Chrome Basin Mixer", "Fittings", 3200), Product("Angle Valve", "Fittings", 650),
        Product("Shower Set", "Bath", 4500), Product("Floor Drain", "Bath", 550),
        Product("Water Tank", "Tanks", 8500), Product("Hand Shower", "Bath", 1250)
    )
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }
    var cart by remember { mutableStateOf(mapOf<String, Int>()) }
    var showCart by remember { mutableStateOf(false) }
    val categories = listOf("All", "Pipes", "Fittings", "Bath", "Tanks")
    val visible = products.filter { (category == "All" || it.category == category) && it.name.contains(query, true) }
    val total = products.sumOf { it.price * (cart[it.name] ?: 0) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Navy, secondary = Color(0xFFE0AD48))) {
        Scaffold(topBar = {
            TopAppBar(title = { Column {
                Text("INSAAF", fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("SANITARY STORE", fontSize = 11.sp, letterSpacing = 3.sp)
            } }, actions = {
                BadgedBox(badge = { if (cart.values.sum() > 0) Badge { Text(cart.values.sum().toString()) } }) {
                    IconButton(onClick = { showCart = !showCart }) { Icon(Icons.Default.ShoppingCart, "Cart") }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy, titleContentColor = Color.White, actionIconContentColor = Color.White))
        }) { padding ->
            if (showCart) {
                Column(Modifier.fillMaxSize().padding(padding).padding(18.dp)) {
                    Text("Your order", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
                    val selected = products.filter { (cart[it.name] ?: 0) > 0 }
                    if (selected.isEmpty()) Text("Your cart is empty.")
                    selected.forEach { product ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(product.name, fontWeight = FontWeight.SemiBold)
                                Text("Rs. ${product.price} × ${cart[product.name]}")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { val n = (cart[product.name] ?: 0) - 1; cart = if (n <= 0) cart - product.name else cart + (product.name to n) }) { Icon(Icons.Default.Remove, "Remove") }
                                Text("${cart[product.name]}")
                                IconButton(onClick = { cart = cart + (product.name to ((cart[product.name] ?: 0) + 1)) }) { Icon(Icons.Default.Add, "Add") }
                            }
                        }
                        HorizontalDivider()
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Estimated total: Rs. $total", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Text("Demo prices only. Confirm current rates and availability with the store.", color = Color.Gray, fontSize = 12.sp)
                    Button(onClick = { showCart = false }, modifier = Modifier.fillMaxWidth()) { Text("Continue shopping") }
                }
            } else {
                LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                    item {
                        Spacer(Modifier.height(18.dp))
                        Text("Everything for your", fontSize = 16.sp, color = Color.DarkGray)
                        Text("bathroom & plumbing.", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Text("Quality sanitary and plumbing essentials", color = Color.Gray)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Search products") }, shape = RoundedCornerShape(14.dp))
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            categories.forEach { c -> FilterChip(selected = category == c, onClick = { category = c }, label = { Text(c) }) }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("PRODUCTS", color = Navy, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    }
                    items(visible) { product ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(48.dp).background(Color(0xFFEAF0EC), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                    Text("◆", color = Navy, fontSize = 23.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(product.name, fontWeight = FontWeight.Bold, color = Navy)
                                    Text(product.category, fontSize = 11.sp, color = Color.Gray)
                                    Text("Rs. ${product.price} (sample)", color = Navy, fontWeight = FontWeight.Bold)
                                }
                                FilledIconButton(onClick = { cart = cart + (product.name to ((cart[product.name] ?: 0) + 1)) }) {
                                    Icon(Icons.Default.Add, "Add to cart")
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(12.dp))
                        Text("INSAAF SANITARY STORE", color = Navy, fontWeight = FontWeight.Bold)
                        Text("Sample products and prices. Replace with your actual inventory and rates.", color = Color.Gray, fontSize = 11.sp)
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
