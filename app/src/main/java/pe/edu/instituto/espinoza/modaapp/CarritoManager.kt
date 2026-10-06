package pe.edu.instituto.espinoza.modaapp

import pe.edu.instituto.espinoza.modaapp.model.CarritoItem
import pe.edu.instituto.espinoza.modaapp.model.Ropa

object CarritoManager {

    private val items = mutableListOf<CarritoItem>()

    fun agregar(ropa: Ropa, cantidad: Int) {

        val existente = items.find {
            it.ropa.id == ropa.id
        }

        if (existente != null) {
            existente.cantidad += cantidad
        } else {
            items.add(
                CarritoItem(
                    ropa = ropa,
                    cantidad = cantidad
                )
            )
        }
    }

    fun obtenerItems(): MutableList<CarritoItem> {
        return items
    }

    fun eliminar(ropaId: Int?) {
        items.removeAll {
            it.ropa.id == ropaId
        }
    }

    fun actualizarCantidad(
        ropaId: Int?,
        cantidad: Int
    ) {
        val item = items.find {
            it.ropa.id == ropaId
        }

        if (item != null) {
            item.cantidad = cantidad
        }
    }

    fun calcularTotal(): Double {
        return items.sumOf {
            it.subtotal()
        }
    }

    fun vaciar() {
        items.clear()
    }

    fun estaVacio(): Boolean {
        return items.isEmpty()
    }
}