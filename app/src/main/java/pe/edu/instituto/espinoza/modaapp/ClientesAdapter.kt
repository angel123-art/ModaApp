package pe.edu.instituto.espinoza.modaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClientesAdapter(
    private var lista: MutableList<ClienteResumen>
) : RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvNombre =
            itemView.findViewById<TextView>(
                R.id.tvNombreCliente
            )

        val tvTelefono =
            itemView.findViewById<TextView>(
                R.id.tvTelefonoCliente
            )

        val tvPedidos =
            itemView.findViewById<TextView>(
                R.id.tvPedidosCliente
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClienteViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_cliente,
            parent,
            false
        )

        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ClienteViewHolder,
        position: Int
    ) {

        val cliente = lista[position]

        holder.tvNombre.text =
            cliente.nombre

        holder.tvTelefono.text =
            "Teléfono: ${cliente.telefono}"

        holder.tvPedidos.text =
            "Pedidos realizados: ${cliente.cantidadPedidos}"
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun actualizarLista(
        nuevaLista: MutableList<ClienteResumen>
    ) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}