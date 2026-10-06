package pe.edu.instituto.espinoza.modaapp.model

data class CarritoItem(
    val ropa: Ropa,
    var cantidad: Int
) {
    fun subtotal(): Double {
        return ropa.precio * cantidad
    }
}