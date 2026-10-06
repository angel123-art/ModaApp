package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pe.edu.instituto.espinoza.modaapp.database.DBHelper

class PedidoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pedido)

        dbHelper = DBHelper(this)

        val etNombreCliente =
            findViewById<TextInputEditText>(R.id.etNombreCliente)

        val etTelefonoCliente =
            findViewById<TextInputEditText>(R.id.etTelefonoCliente)

        val tvResumenPedido =
            findViewById<TextView>(R.id.tvResumenPedido)

        val tvTotalPedido =
            findViewById<TextView>(R.id.tvTotalPedido)

        val btnConfirmarPedido =
            findViewById<MaterialButton>(R.id.btnConfirmarPedido)

        // ==========================================
        // OBTENER CARRITO
        // ==========================================

        val items = CarritoManager.obtenerItems()

        if (items.isEmpty()) {

            Toast.makeText(
                this,
                "El carrito está vacío",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        // ==========================================
        // MOSTRAR RESUMEN DEL PEDIDO
        // ==========================================

        val resumen = StringBuilder()

        for (item in items) {

            resumen.append("${item.ropa.nombre}\n")

            resumen.append(
                "Cantidad: ${item.cantidad}\n"
            )

            resumen.append(
                "Precio: S/ %.2f\n".format(
                    item.ropa.precio
                )
            )

            resumen.append(
                "Subtotal: S/ %.2f\n\n".format(
                    item.subtotal()
                )
            )
        }

        tvResumenPedido.text = resumen.toString()

        val total = CarritoManager.calcularTotal()

        tvTotalPedido.text =
            "Total: S/ %.2f".format(total)

        // ==========================================
        // CONFIRMAR PEDIDO
        // ==========================================

        btnConfirmarPedido.setOnClickListener {

            val nombre =
                etNombreCliente.text.toString().trim()

            val telefono =
                etTelefonoCliente.text.toString().trim()

            // ======================================
            // VALIDAR NOMBRE
            // ======================================

            if (nombre.isEmpty()) {

                etNombreCliente.error =
                    "Ingrese el nombre del cliente"

                etNombreCliente.requestFocus()

                return@setOnClickListener
            }

            // ======================================
            // VALIDAR TELÉFONO
            // ======================================

            if (telefono.length != 9) {

                etTelefonoCliente.error =
                    "Ingrese un teléfono de 9 dígitos"

                etTelefonoCliente.requestFocus()

                return@setOnClickListener
            }

            // ======================================
            // REGISTRAR CLIENTE
            // ======================================

            val clienteId =
                dbHelper.insertarCliente(
                    nombre,
                    telefono
                )

            if (clienteId == -1L) {

                Toast.makeText(
                    this,
                    "No se pudo registrar el cliente",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // ======================================
            // REGISTRAR PEDIDO
            // ======================================

            val pedidoId =
                dbHelper.insertarPedido(
                    clienteId,
                    total
                )

            if (pedidoId == -1L) {

                Toast.makeText(
                    this,
                    "No se pudo registrar el pedido",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // ======================================
            // REGISTRAR DETALLES
            // ======================================

            for (item in items) {

                val ropaId = item.ropa.id

                if (ropaId != null) {

                    dbHelper.insertarDetallePedido(
                        pedidoId,
                        ropaId,
                        item.cantidad,
                        item.ropa.precio
                    )

                    dbHelper.descontarStock(
                        ropaId,
                        item.cantidad
                    )
                }
            }

            // ======================================
            // VACIAR CARRITO
            // ======================================

            CarritoManager.vaciar()

            Toast.makeText(
                this,
                "Pedido registrado correctamente",
                Toast.LENGTH_LONG
            ).show()

            finish()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}