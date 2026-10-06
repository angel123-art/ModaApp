package pe.edu.instituto.espinoza.modaapp.database


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.edu.instituto.espinoza.modaapp.model.Ropa
import pe.edu.instituto.espinoza.modaapp.PedidoResumen
import pe.edu.instituto.espinoza.modaapp.ClienteResumen
import pe.edu.instituto.espinoza.modaapp.Estadisticas
class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "modaapp.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {

        // ==========================================
        // USUARIOS
        // ==========================================

        db.execSQL(
            """
            CREATE TABLE usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                usuario TEXT NOT NULL UNIQUE,
                clave TEXT NOT NULL,
                rol TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Administrador inicial
        val admin = ContentValues().apply {
            put("nombre", "Administrador")
            put("usuario", "admin")
            put("clave", "1234")
            put("rol", "ADMIN")
        }

        db.insert("usuario", null, admin)


        // ==========================================
        // CATEGORÍAS
        // ==========================================

        db.execSQL(
            """
            CREATE TABLE categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            )
            """.trimIndent()
        )

        val categorias = listOf(
            "Polos",
            "Camisas",
            "Pantalones",
            "Vestidos",
            "Casacas",
            "Accesorios"
        )

        for (categoria in categorias) {

            val values = ContentValues().apply {
                put("nombre", categoria)
            }

            db.insert(
                "categoria",
                null,
                values
            )
        }


        // ==========================================
        // ROPA
        // ==========================================

        db.execSQL(
            """
            CREATE TABLE ropa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                codigo TEXT NOT NULL UNIQUE,
                nombre TEXT NOT NULL,
                categoria TEXT NOT NULL,
                talla TEXT NOT NULL,
                color TEXT NOT NULL,
                precio REAL NOT NULL,
                stock INTEGER NOT NULL
            )
            """.trimIndent()
        )


        // ==========================================
        // CLIENTES
        // ==========================================

        db.execSQL(
            """
            CREATE TABLE cliente (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                telefono TEXT NOT NULL
            )
            """.trimIndent()
        )


        // ==========================================
        // PEDIDOS
        // ==========================================

        db.execSQL(
            """
            CREATE TABLE pedido (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                total REAL NOT NULL,
                estado TEXT NOT NULL DEFAULT 'PENDIENTE',

                FOREIGN KEY(cliente_id)
                    REFERENCES cliente(id)
            )
            """.trimIndent()
        )


        // ==========================================
        // DETALLE PEDIDO
        // ==========================================

        db.execSQL(
            """
            CREATE TABLE detalle_pedido (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pedido_id INTEGER NOT NULL,
                ropa_id INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                precio REAL NOT NULL,
                subtotal REAL NOT NULL,

                FOREIGN KEY(pedido_id)
                    REFERENCES pedido(id),

                FOREIGN KEY(ropa_id)
                    REFERENCES ropa(id)
            )
            """.trimIndent()
        )
    }


    // ==========================================
    // ACTUALIZACIÓN DE BASE DE DATOS
    // ==========================================

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        if (oldVersion < 2) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS cliente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    telefono TEXT NOT NULL
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    fecha TEXT NOT NULL,
                    total REAL NOT NULL,
                    estado TEXT NOT NULL DEFAULT 'PENDIENTE',

                    FOREIGN KEY(cliente_id)
                        REFERENCES cliente(id)
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS detalle_pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pedido_id INTEGER NOT NULL,
                    ropa_id INTEGER NOT NULL,
                    cantidad INTEGER NOT NULL,
                    precio REAL NOT NULL,
                    subtotal REAL NOT NULL,

                    FOREIGN KEY(pedido_id)
                        REFERENCES pedido(id),

                    FOREIGN KEY(ropa_id)
                        REFERENCES ropa(id)
                )
                """.trimIndent()
            )
        }
    }


    // ==========================================
    // INSERTAR ROPA
    // ==========================================

    fun insertarRopa(ropa: Ropa): Long {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("codigo", ropa.codigo)
            put("nombre", ropa.nombre)
            put("categoria", ropa.categoria)
            put("talla", ropa.talla)
            put("color", ropa.color)
            put("precio", ropa.precio)
            put("stock", ropa.stock)
        }

        return db.insert(
            "ropa",
            null,
            values
        )
    }


    // ==========================================
    // LISTAR ROPA
    // ==========================================

    fun obtenerRopa(): MutableList<Ropa> {

        val lista = mutableListOf<Ropa>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ropa ORDER BY id DESC",
            null
        )

        while (cursor.moveToNext()) {

            val ropa = Ropa(

                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                ),

                codigo = cursor.getString(
                    cursor.getColumnIndexOrThrow("codigo")
                ),

                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),

                categoria = cursor.getString(
                    cursor.getColumnIndexOrThrow("categoria")
                ),

                talla = cursor.getString(
                    cursor.getColumnIndexOrThrow("talla")
                ),

                color = cursor.getString(
                    cursor.getColumnIndexOrThrow("color")
                ),

                precio = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("precio")
                ),

                stock = cursor.getInt(
                    cursor.getColumnIndexOrThrow("stock")
                )
            )

            lista.add(ropa)
        }

        cursor.close()

        return lista
    }


    // ==========================================
    // ACTUALIZAR ROPA
    // ==========================================

    fun actualizarRopa(ropa: Ropa): Int {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("codigo", ropa.codigo)
            put("nombre", ropa.nombre)
            put("categoria", ropa.categoria)
            put("talla", ropa.talla)
            put("color", ropa.color)
            put("precio", ropa.precio)
            put("stock", ropa.stock)
        }

        return db.update(
            "ropa",
            values,
            "id = ?",
            arrayOf(
                ropa.id.toString()
            )
        )
    }


    // ==========================================
    // ELIMINAR ROPA
    // ==========================================

    fun eliminarRopa(id: Int): Int {

        val db = writableDatabase

        return db.delete(
            "ropa",
            "id = ?",
            arrayOf(
                id.toString()
            )
        )
    }


    // ==========================================
    // VALIDAR CÓDIGO
    // ==========================================

    fun existeCodigo(
        codigo: String,
        idExcluir: Int? = null
    ): Boolean {

        val db = readableDatabase

        val query: String
        val args: Array<String>

        if (idExcluir == null) {

            query =
                "SELECT id FROM ropa WHERE codigo = ?"

            args = arrayOf(codigo)

        } else {

            query =
                "SELECT id FROM ropa WHERE codigo = ? AND id != ?"

            args = arrayOf(
                codigo,
                idExcluir.toString()
            )
        }

        val cursor = db.rawQuery(
            query,
            args
        )

        val existe =
            cursor.moveToFirst()

        cursor.close()

        return existe
    }


    // ==========================================
    // INSERTAR CLIENTE
    // ==========================================

    fun insertarCliente(
        nombre: String,
        telefono: String
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("nombre", nombre)
            put("telefono", telefono)
        }

        return db.insert(
            "cliente",
            null,
            values
        )
    }


    // ==========================================
    // INSERTAR PEDIDO
    // ==========================================

    fun insertarPedido(
        clienteId: Long,
        total: Double
    ): Long {

        val db = writableDatabase

        val fecha =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()
            ).format(
                java.util.Date()
            )

        val values = ContentValues().apply {

            put("cliente_id", clienteId)
            put("fecha", fecha)
            put("total", total)
            put("estado", "PENDIENTE")
        }

        return db.insert(
            "pedido",
            null,
            values
        )
    }


    // ==========================================
    // INSERTAR DETALLE DEL PEDIDO
    // ==========================================

    fun insertarDetallePedido(
        pedidoId: Long,
        ropaId: Int,
        cantidad: Int,
        precio: Double
    ): Long {

        val db = writableDatabase

        val subtotal =
            cantidad * precio

        val values = ContentValues().apply {

            put("pedido_id", pedidoId)
            put("ropa_id", ropaId)
            put("cantidad", cantidad)
            put("precio", precio)
            put("subtotal", subtotal)
        }

        return db.insert(
            "detalle_pedido",
            null,
            values
        )
    }


    // ==========================================
    // DESCONTAR STOCK
    // ==========================================

    fun descontarStock(
        ropaId: Int,
        cantidad: Int
    ): Int {

        val db = writableDatabase

        val cursor = db.rawQuery(
            """
            SELECT stock
            FROM ropa
            WHERE id = ?
            """.trimIndent(),
            arrayOf(
                ropaId.toString()
            )
        )

        if (!cursor.moveToFirst()) {

            cursor.close()

            return 0
        }

        val stockActual =
            cursor.getInt(0)

        cursor.close()

        if (stockActual < cantidad) {
            return 0
        }

        val nuevoStock =
            stockActual - cantidad

        val values = ContentValues().apply {
            put("stock", nuevoStock)
        }

        return db.update(
            "ropa",
            values,
            "id = ?",
            arrayOf(
                ropaId.toString()
            )
        )



    }
    // ==========================================
// LISTAR PEDIDOS
// ==========================================

    fun obtenerPedidos(): MutableList<PedidoResumen> {

        val lista = mutableListOf<PedidoResumen>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT
            p.id,
            c.nombre,
            c.telefono,
            p.fecha,
            p.total,
            p.estado
        FROM pedido p
        INNER JOIN cliente c
            ON p.cliente_id = c.id
        ORDER BY p.id DESC
        """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val pedido = PedidoResumen(
                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                ),
                cliente = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                fecha = cursor.getString(
                    cursor.getColumnIndexOrThrow("fecha")
                ),
                total = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("total")
                ),
                estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
                )
            )

            lista.add(pedido)
        }

        cursor.close()

        return lista
    }


// ==========================================
// ACTUALIZAR ESTADO DEL PEDIDO
// ==========================================

    fun actualizarEstadoPedido(
        pedidoId: Int,
        nuevoEstado: String
    ): Int {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("estado", nuevoEstado)
        }

        return db.update(
            "pedido",
            values,
            "id = ?",
            arrayOf(
                pedidoId.toString()
            )
        )
    }
    // ==========================================
// LISTAR CLIENTES
// ==========================================

    fun obtenerClientes(): MutableList<ClienteResumen> {

        val lista = mutableListOf<ClienteResumen>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT
            c.id,
            c.nombre,
            c.telefono,
            COUNT(p.id) AS cantidad_pedidos
        FROM cliente c
        LEFT JOIN pedido p
            ON c.id = p.cliente_id
        GROUP BY
            c.id,
            c.nombre,
            c.telefono
        ORDER BY c.id DESC
        """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val cliente = ClienteResumen(
                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                cantidadPedidos = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        "cantidad_pedidos"
                    )
                )
            )

            lista.add(cliente)
        }

        cursor.close()

        return lista
    }
    // ==========================================
// OBTENER ESTADÍSTICAS
// ==========================================

    fun obtenerEstadisticas(): Estadisticas {

        val db = readableDatabase

        // Total de ropa
        val cursorRopa = db.rawQuery(
            "SELECT COUNT(*) FROM ropa",
            null
        )

        cursorRopa.moveToFirst()

        val totalRopa = cursorRopa.getInt(0)

        cursorRopa.close()


        // Total de clientes
        val cursorClientes = db.rawQuery(
            "SELECT COUNT(*) FROM cliente",
            null
        )

        cursorClientes.moveToFirst()

        val totalClientes = cursorClientes.getInt(0)

        cursorClientes.close()


        // Total de pedidos
        val cursorPedidos = db.rawQuery(
            "SELECT COUNT(*) FROM pedido",
            null
        )

        cursorPedidos.moveToFirst()

        val totalPedidos = cursorPedidos.getInt(0)

        cursorPedidos.close()


        // Pedidos pendientes
        val cursorPendientes = db.rawQuery(
            """
        SELECT COUNT(*)
        FROM pedido
        WHERE estado = 'PENDIENTE'
        """.trimIndent(),
            null
        )

        cursorPendientes.moveToFirst()

        val pedidosPendientes = cursorPendientes.getInt(0)

        cursorPendientes.close()


        // Pedidos atendidos
        val cursorAtendidos = db.rawQuery(
            """
        SELECT COUNT(*)
        FROM pedido
        WHERE estado = 'ATENDIDO'
        """.trimIndent(),
            null
        )

        cursorAtendidos.moveToFirst()

        val pedidosAtendidos = cursorAtendidos.getInt(0)

        cursorAtendidos.close()


        // Total de ventas
        val cursorVentas = db.rawQuery(
            """
        SELECT COALESCE(SUM(total), 0)
        FROM pedido
        WHERE estado = 'ATENDIDO'
        """.trimIndent(),
            null
        )

        cursorVentas.moveToFirst()

        val totalVentas = cursorVentas.getDouble(0)

        cursorVentas.close()


        return Estadisticas(
            totalRopa = totalRopa,
            totalClientes = totalClientes,
            totalPedidos = totalPedidos,
            pedidosPendientes = pedidosPendientes,
            pedidosAtendidos = pedidosAtendidos,
            totalVentas = totalVentas
        )
    }
    // ==========================================
// VALIDAR USUARIO
// ==========================================

    fun validarUsuario(
        usuario: String,
        clave: String
    ): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT id
        FROM usuario
        WHERE usuario = ?
        AND clave = ?
        AND rol = 'ADMIN'
        """.trimIndent(),
            arrayOf(
                usuario,
                clave
            )
        )

        val valido = cursor.moveToFirst()

        cursor.close()

        return valido
    }
}
