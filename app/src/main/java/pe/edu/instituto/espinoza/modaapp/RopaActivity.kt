package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.database.DBHelper
import pe.edu.instituto.espinoza.modaapp.model.Ropa

class RopaActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: RopaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ropa)

        dbHelper = DBHelper(this)

        val recycler = findViewById<RecyclerView>(R.id.recyclerRopa)
        adapter = RopaAdapter(dbHelper.obtenerRopa()) { ropa ->
            Toast.makeText(this, " seleccionado", Toast.LENGTH_SHORT).show()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val btnAgregar = findViewById<MaterialButton>(R.id.btnAgregarRopa)
        val btnRegresar = findViewById<MaterialButton>(R.id.btnRegresarRopa)

        btnAgregar.setOnClickListener {
            mostrarDialogoAgregar()
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun mostrarDialogoAgregar() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etCodigo = EditText(this).apply { hint = "CÃ³digo Ãºnico (ej: POL01)" }
        val etNombre = EditText(this).apply { hint = "Nombre de la prenda" }
        val etCategoria = EditText(this).apply { hint = "CategorÃ­a (ej: Polos)" }
        val etTalla = EditText(this).apply { hint = "Talla (S, M, L, XL)" }
        val etColor = EditText(this).apply { hint = "Color" }
        val etPrecio = EditText(this).apply { hint = "Precio (ej: 49.90)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL }
        val etStock = EditText(this).apply { hint = "Stock (ej: 10)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER }

        layout.addView(etCodigo)
        layout.addView(etNombre)
        layout.addView(etCategoria)
        layout.addView(etTalla)
        layout.addView(etColor)
        layout.addView(etPrecio)
        layout.addView(etStock)

        AlertDialog.Builder(this)
            .setTitle("Registrar Ropa")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val codigo = etCodigo.text.toString().trim()
                val nombre = etNombre.text.toString().trim()
                val categoria = etCategoria.text.toString().trim()
                val talla = etTalla.text.toString().trim()
                val color = etColor.text.toString().trim()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val stock = etStock.text.toString().toIntOrNull() ?: 0

                if (codigo.isNotEmpty() && nombre.isNotEmpty() && precio > 0) {
                    val ropa = Ropa(
                        codigo = codigo,
                        nombre = nombre,
                        categoria = if (categoria.isEmpty()) "Polos" else categoria,
                        talla = if (talla.isEmpty()) "M" else talla,
                        color = if (color.isEmpty()) "Negro" else color,
                        precio = precio,
                        stock = stock
                    )
                    dbHelper.insertarRopa(ropa)
                    adapter.actualizarLista(dbHelper.obtenerRopa())
                    Toast.makeText(this, "Prenda registrada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
