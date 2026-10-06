package pe.edu.instituto.espinoza.modaapp

data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val cantidadPedidos: Int
)