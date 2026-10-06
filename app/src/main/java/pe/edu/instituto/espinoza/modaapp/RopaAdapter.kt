package pe.edu.instituto.espinoza.modaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.instituto.espinoza.modaapp.model.Ropa

class RopaAdapter(
    private var lista: MutableList<Ropa>,
    private val onClick: (Ropa) -> Unit
) : RecyclerView.Adapter<RopaAdapter.RopaViewHolder>() {

    class RopaViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNombre =
            itemView.findViewById<TextView>(R.id.tvNombreRopa)

        val tvCodigo =
            itemView.findViewById<TextView>(R.id.tvCodigoRopa)

        val tvDetalles =
            itemView.findViewById<TextView>(R.id.tvDetallesRopa)

        val tvPrecioStock =
            itemView.findViewById<TextView>(R.id.tvPrecioStock)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RopaViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_ropa,
                parent,
                false
            )

        return RopaViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RopaViewHolder,
        position: Int
    ) {

        val ropa = lista[position]

        holder.tvNombre.text = ropa.nombre

        holder.tvCodigo.text =
            "Código: ${ropa.codigo}"

        holder.tvDetalles.text =
            "${ropa.talla} | ${ropa.color} | ${ropa.categoria}"

        holder.tvPrecioStock.text =
            "S/ %.2f | Stock: %d".format(
                ropa.precio,
                ropa.stock
            )

        holder.itemView.setOnClickListener {
            onClick(ropa)
        }
    }

    override fun getItemCount(): Int =
        lista.size

    fun actualizarLista(
        nuevaLista: MutableList<Ropa>
    ) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}