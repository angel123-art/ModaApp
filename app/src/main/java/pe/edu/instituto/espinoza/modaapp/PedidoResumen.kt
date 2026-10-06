package pe.edu.instituto.espinoza.modaapp

data class PedidoResumen(
    val id: Int,
    val cliente: String,
    val telefono: String,
    val fecha: String,
    val total: Double,
    val estado: String
)