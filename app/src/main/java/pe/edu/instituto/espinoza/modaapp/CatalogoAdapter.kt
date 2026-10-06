package pe.edu.instituto.espinoza.modaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.instituto.espinoza.modaapp.model.Ropa

class CatalogoAdapter(
    private var lista: MutableList<Ropa>,
    private val onClick: (Ropa) -> Unit
) : RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder>() {

    class CatalogoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvNombre =
            itemView.findViewById<TextView>(
                R.id.tvNombreCatalogo
            )

        val tvCategoria =
            itemView.findViewById<TextView>(
                R.id.tvCategoriaCatalogo
            )

        val tvDetalles =
            itemView.findViewById<TextView>(
                R.id.tvDetallesCatalogo
            )

        val tvPrecio =
            itemView.findViewById<TextView>(
                R.id.tvPrecioCatalogo
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogoViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_catalogo,
            parent,
            false
        )

        return CatalogoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CatalogoViewHolder,
        position: Int
    ) {

        val ropa = lista[position]

        holder.tvNombre.text = ropa.nombre

        holder.tvCategoria.text =
            ropa.categoria

        holder.tvDetalles.text =
            "Talla: ${ropa.talla} | Color: ${ropa.color}"

        holder.tvPrecio.text =
            "S/ %.2f".format(ropa.precio)

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