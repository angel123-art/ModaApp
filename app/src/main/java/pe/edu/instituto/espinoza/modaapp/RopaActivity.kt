package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import android.text.InputType
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

        val recycler =
            findViewById<RecyclerView>(R.id.recyclerRopa)

        adapter = RopaAdapter(
            dbHelper.obtenerRopa()
        ) { ropa ->

            mostrarOpciones(ropa)
        }

        recycler.layoutManager =
            LinearLayoutManager(this)

        recycler.adapter = adapter

        val btnAgregar =
            findViewById<MaterialButton>(
                R.id.btnAgregarRopa
            )

        val btnRegresar =
            findViewById<MaterialButton>(
                R.id.btnRegresarRopa
            )

        btnAgregar.setOnClickListener {
            mostrarFormulario()
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    // ==================================================
    // AGREGAR
    // ==================================================

    private fun mostrarFormulario() {

        val formulario = crearFormulario()

        AlertDialog.Builder(this)
            .setTitle("Agregar ropa")
            .setView(formulario.contenedor)
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                "Guardar",
                null
            )
            .create()
            .apply {

                setOnShowListener {

                    getButton(
                        AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener {

                        val codigo =
                            formulario.codigo.text.toString().trim()

                        val nombre =
                            formulario.nombre.text.toString().trim()

                        val categoria =
                            formulario.categoria.text.toString().trim()

                        val talla =
                            formulario.talla.text.toString().trim()

                        val color =
                            formulario.color.text.toString().trim()

                        val precio =
                            formulario.precio.text.toString()
                                .toDoubleOrNull()

                        val stock =
                            formulario.stock.text.toString()
                                .toIntOrNull()

                        if (
                            codigo.isEmpty() ||
                            nombre.isEmpty() ||
                            categoria.isEmpty() ||
                            talla.isEmpty() ||
                            color.isEmpty() ||
                            precio == null ||
                            stock == null
                        ) {

                            Toast.makeText(
                                this@RopaActivity,
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        if (precio < 0) {

                            Toast.makeText(
                                this@RopaActivity,
                                "El precio no puede ser negativo",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        if (stock < 0) {

                            Toast.makeText(
                                this@RopaActivity,
                                "El stock no puede ser negativo",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        if (
                            dbHelper.existeCodigo(codigo)
                        ) {

                            Toast.makeText(
                                this@RopaActivity,
                                "El código ya existe",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        val ropa = Ropa(
                            codigo = codigo,
                            nombre = nombre,
                            categoria = categoria,
                            talla = talla,
                            color = color,
                            precio = precio,
                            stock = stock
                        )

                        val resultado =
                            dbHelper.insertarRopa(ropa)

                        if (resultado > 0) {

                            Toast.makeText(
                                this@RopaActivity,
                                "Ropa registrada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            actualizarLista()

                            dismiss()

                        } else {

                            Toast.makeText(
                                this@RopaActivity,
                                "No se pudo registrar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                show()
            }
    }

    // ==================================================
    // OPCIONES EDITAR / ELIMINAR
    // ==================================================

    private fun mostrarOpciones(ropa: Ropa) {

        AlertDialog.Builder(this)
            .setTitle(ropa.nombre)
            .setItems(
                arrayOf(
                    "Editar",
                    "Eliminar"
                )
            ) { _, which ->

                when (which) {

                    0 -> mostrarEditar(ropa)

                    1 -> confirmarEliminar(ropa)
                }
            }
            .show()
    }

    // ==================================================
    // EDITAR
    // ==================================================

    private fun mostrarEditar(ropa: Ropa) {

        val formulario = crearFormulario(ropa)

        AlertDialog.Builder(this)
            .setTitle("Editar ropa")
            .setView(formulario.contenedor)
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                "Actualizar",
                null
            )
            .create()
            .apply {

                setOnShowListener {

                    getButton(
                        AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener {

                        val codigo =
                            formulario.codigo.text.toString().trim()

                        val nombre =
                            formulario.nombre.text.toString().trim()

                        val categoria =
                            formulario.categoria.text.toString().trim()

                        val talla =
                            formulario.talla.text.toString().trim()

                        val color =
                            formulario.color.text.toString().trim()

                        val precio =
                            formulario.precio.text.toString()
                                .toDoubleOrNull()

                        val stock =
                            formulario.stock.text.toString()
                                .toIntOrNull()

                        if (
                            codigo.isEmpty() ||
                            nombre.isEmpty() ||
                            categoria.isEmpty() ||
                            talla.isEmpty() ||
                            color.isEmpty() ||
                            precio == null ||
                            stock == null
                        ) {

                            Toast.makeText(
                                this@RopaActivity,
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        if (precio < 0 || stock < 0) {

                            Toast.makeText(
                                this@RopaActivity,
                                "Precio y stock deben ser válidos",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        if (
                            dbHelper.existeCodigo(
                                codigo,
                                ropa.id
                            )
                        ) {

                            Toast.makeText(
                                this@RopaActivity,
                                "El código ya existe",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }

                        val actualizada = Ropa(

                            id = ropa.id,

                            codigo = codigo,

                            nombre = nombre,

                            categoria = categoria,

                            talla = talla,

                            color = color,

                            precio = precio,

                            stock = stock
                        )

                        val resultado =
                            dbHelper.actualizarRopa(
                                actualizada
                            )

                        if (resultado > 0) {

                            Toast.makeText(
                                this@RopaActivity,
                                "Ropa actualizada",
                                Toast.LENGTH_SHORT
                            ).show()

                            actualizarLista()

                            dismiss()

                        } else {

                            Toast.makeText(
                                this@RopaActivity,
                                "No se pudo actualizar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                show()
            }
    }

    // ==================================================
    // ELIMINAR
    // ==================================================

    private fun confirmarEliminar(ropa: Ropa) {

        AlertDialog.Builder(this)

            .setTitle("Eliminar ropa")

            .setMessage(
                "¿Deseas eliminar \"${ropa.nombre}\"?"
            )

            .setNegativeButton(
                "Cancelar",
                null
            )

            .setPositiveButton(
                "Eliminar"
            ) { _, _ ->

                val resultado =
                    dbHelper.eliminarRopa(
                        ropa.id!!
                    )

                if (resultado > 0) {

                    Toast.makeText(
                        this,
                        "Ropa eliminada",
                        Toast.LENGTH_SHORT
                    ).show()

                    actualizarLista()

                } else {

                    Toast.makeText(
                        this,
                        "No se pudo eliminar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            .show()
    }

    // ==================================================
    // FORMULARIO
    // ==================================================

    private fun crearFormulario(
        ropa: Ropa? = null
    ): Formulario {

        val contenedor =
            LinearLayout(this)

        contenedor.orientation =
            LinearLayout.VERTICAL

        contenedor.setPadding(
            50,
            10,
            50,
            10
        )

        val codigo = EditText(this)
        codigo.hint = "Código"
        codigo.setText(ropa?.codigo ?: "")

        val nombre = EditText(this)
        nombre.hint = "Nombre"
        nombre.setText(ropa?.nombre ?: "")

        val categoria = EditText(this)
        categoria.hint = "Categoría"
        categoria.setText(ropa?.categoria ?: "")

        val talla = EditText(this)
        talla.hint = "Talla"
        talla.setText(ropa?.talla ?: "")

        val color = EditText(this)
        color.hint = "Color"
        color.setText(ropa?.color ?: "")

        val precio = EditText(this)
        precio.hint = "Precio"
        precio.inputType =
            InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL
        precio.setText(
            ropa?.precio?.toString() ?: ""
        )

        val stock = EditText(this)
        stock.hint = "Stock"
        stock.inputType =
            InputType.TYPE_CLASS_NUMBER
        stock.setText(
            ropa?.stock?.toString() ?: ""
        )

        contenedor.addView(codigo)
        contenedor.addView(nombre)
        contenedor.addView(categoria)
        contenedor.addView(talla)
        contenedor.addView(color)
        contenedor.addView(precio)
        contenedor.addView(stock)

        return Formulario(
            contenedor,
            codigo,
            nombre,
            categoria,
            talla,
            color,
            precio,
            stock
        )
    }

    // ==================================================
    // ACTUALIZAR LISTA
    // ==================================================

    private fun actualizarLista() {

        adapter.actualizarLista(
            dbHelper.obtenerRopa()
        )
    }

    // ==================================================
    // CLASE FORMULARIO
    // ==================================================

    private data class Formulario(

        val contenedor: LinearLayout,

        val codigo: EditText,

        val nombre: EditText,

        val categoria: EditText,

        val talla: EditText,

        val color: EditText,

        val precio: EditText,

        val stock: EditText
    )
}