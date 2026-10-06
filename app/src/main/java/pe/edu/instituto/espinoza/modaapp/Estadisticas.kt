package pe.edu.instituto.espinoza.modaapp

data class Estadisticas(
    val totalRopa: Int,
    val totalClientes: Int,
    val totalPedidos: Int,
    val pedidosPendientes: Int,
    val pedidosAtendidos: Int,
    val totalVentas: Double
)