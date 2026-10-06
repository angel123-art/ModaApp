package pe.edu.instituto.espinoza.modaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.model.CarritoItem

class CarritoAdapter(
    private val lista: MutableList<pe.edu.instituto.espinoza.modaapp.model.CarritoItem>,
    private val onEliminar: (CarritoItem) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvNombre =
            itemView.findViewById<TextView>(
                R.id.tvNombreCarrito
            )

        val tvCantidad =
            itemView.findViewById<TextView>(
                R.id.tvCantidadCarrito
            )

        val tvPrecio =
            itemView.findViewById<TextView>(
                R.id.tvPrecioCarrito
            )

        val tvSubtotal =
            itemView.findViewById<TextView>(
                R.id.tvSubtotalCarrito
            )

        val btnEliminar =
            itemView.findViewById<MaterialButton>(
                R.id.btnEliminarCarrito
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarritoViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_carrito,
            parent,
            false
        )

        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CarritoViewHolder,
        position: Int
    ) {

        val item = lista[position]

        holder.tvNombre.text =
            item.ropa.nombre

        holder.tvCantidad.text =
            "Cantidad: ${item.cantidad}"

        holder.tvPrecio.text =
            "Precio: S/ %.2f".format(
                item.ropa.precio
            )

        holder.tvSubtotal.text =
            "Subtotal: S/ %.2f".format(
                item.subtotal()
            )

        holder.btnEliminar.setOnClickListener {
            onEliminar(item)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}