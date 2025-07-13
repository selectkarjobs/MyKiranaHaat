import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

data class CustomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val customNavItems = listOf(
    CustomNavItem("stores", "Stores", Icons.Filled.ShoppingCart),
    CustomNavItem("products", "Products", Icons.Filled.List),
    CustomNavItem("dashboard", "Dashboard", Icons.Filled.Home),
    CustomNavItem("drivers", "Drivers", Icons.Filled.Person),
    CustomNavItem("customers", "Customers", Icons.Filled.Person)
)