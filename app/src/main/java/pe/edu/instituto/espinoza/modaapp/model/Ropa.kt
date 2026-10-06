package pe.edu.instituto.espinoza.modaapp.model

data class Ropa(
    val id: Int? = null,
    val codigo: String,
    val nombre: String,
    val categoria: String,
    val talla: String,
    val color: String,
    val precio: Double,
    val stock: Int
)