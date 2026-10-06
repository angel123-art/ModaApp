package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PedidosAdapter(
    private var lista: MutableList<PedidoResumen>,
    private val onAtender: (PedidoResumen) -> Unit
) : RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNumero =
            itemView.findViewById<TextView>(R.id.tvNumeroPedido)

        val tvCliente =
            itemView.findViewById<TextView>(R.id.tvClientePedido)

        val tvTelefono =
            itemView.findViewById<TextView>(R.id.tvTelefonoPedido)

        val tvFecha =
            itemView.findViewById<TextView>(R.id.tvFechaPedido)

        val tvTotal =
            itemView.findViewById<TextView>(R.id.tvTotalPedido)

        val tvEstado =
            itemView.findViewById<TextView>(R.id.tvEstadoPedido)

        val btnAtender =
            itemView.findViewById<MaterialButton>(R.id.btnAtenderPedido)

        val btnWhatsApp =
            itemView.findViewById<MaterialButton>(R.id.btnWhatsAppPedido)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PedidoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)

        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PedidoViewHolder,
        position: Int
    ) {

        val pedido = lista[position]

        holder.tvNumero.text = "Pedido #${pedido.id}"

        holder.tvCliente.text =
            "Cliente: ${pedido.cliente}"

        holder.tvTelefono.text =
            "Teléfono: ${pedido.telefono}"

        holder.tvFecha.text =
            "Fecha: ${pedido.fecha}"

        holder.tvTotal.text =
            "Total: S/ %.2f".format(pedido.total)

        holder.tvEstado.text =
            "Estado: ${pedido.estado}"

        // ==========================================
        // ATENDER PEDIDO
        // ==========================================

        if (pedido.estado == "ATENDIDO") {

            holder.btnAtender.isEnabled = false
            holder.btnAtender.text = "Pedido atendido"

        } else {

            holder.btnAtender.isEnabled = true
            holder.btnAtender.text = "Marcar como atendido"

            holder.btnAtender.setOnClickListener {

                onAtender(pedido)
            }
        }

        // ==========================================
        // WHATSAPP
        // ==========================================

        holder.btnWhatsApp.setOnClickListener {

            val telefono = pedido.telefono.trim()

            if (telefono.length != 9) {

                Toast.makeText(
                    holder.itemView.context,
                    "El teléfono no es válido",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val mensaje = """
                Hola ${pedido.cliente} 👋
                
                Somos ModaApp - Boutique Moda Urbana.
                
                Su pedido #${pedido.id} ha sido registrado.
                
                Total: S/ %.2f
                Estado: ${pedido.estado}
                
                Gracias por su compra.
            """.trimIndent().format(pedido.total)

            val telefonoPeru = "51$telefono"

            val url =
                "https://wa.me/$telefonoPeru?text=${Uri.encode(mensaje)}"

            try {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )

                holder.itemView.context.startActivity(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    holder.itemView.context,
                    "No se pudo abrir WhatsApp",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    // ==========================================
    // ACTUALIZAR LISTA
    // ==========================================

    fun actualizarLista(
        nuevaLista: MutableList<PedidoResumen>
    ) {

        lista = nuevaLista

        notifyDataSetChanged()
    }
}