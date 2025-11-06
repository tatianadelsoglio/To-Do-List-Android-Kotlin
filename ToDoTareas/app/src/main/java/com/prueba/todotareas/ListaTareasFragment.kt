/*package com.prueba.todotareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.prueba.todotareas.databinding.FragmentListaTareasBinding
import androidx.lifecycle.lifecycleScope
import com.prueba.todotareas.data.local.TareasDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ListaTareasFragment : Fragment() {

    private lateinit var db: TareasDatabase

    private lateinit var binding: FragmentListaTareasBinding
    private lateinit var adapter: TareaAdapter
    private var tareas = mutableListOf<Tarea>()
    private var nextId = 1 // para asignar ID autoincremental

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaTareasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Lista de Tareas"

        db = TareasDatabase.getDatabase(requireContext())

        adapter = TareaAdapter(
            mutableListOf(),
            onCheckChanged = { tarea, estado ->
                tarea.estado = estado
                lifecycleScope.launch(Dispatchers.IO) {
                    db.tareaDao().actualizarTarea(tarea)
                    val lista = db.tareaDao().obtenerTareas()
                    withContext(Dispatchers.Main) { actualizarLista(lista) }
                }
            },
            onItemClick = { tarea ->
                mostrarDialogoEditarEliminar(tarea)
            }
        )


        binding.recyclerViewTareas.adapter = adapter
        binding.recyclerViewTareas.layoutManager = LinearLayoutManager(requireContext())

        // Loading
        binding.progressBar.visibility = View.VISIBLE
        binding.layoutPrincipal.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            val lista = db.tareaDao().obtenerTareas()
            withContext(Dispatchers.Main) {
                actualizarLista(lista)
                binding.progressBar.visibility = View.GONE
                binding.layoutPrincipal.visibility = View.VISIBLE
            }
        }

        binding.fabAgregarTarea.setOnClickListener { mostrarDialogoNuevaTarea() }

    }


    private fun mostrarDialogoNuevaTarea() {
        val dialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_nueva_tarea, null)
        val etTitulo = dialog.findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = dialog.findViewById<EditText>(R.id.etDescripcion)

        AlertDialog.Builder(requireContext())
            .setTitle("Nueva Tarea")
            .setView(dialog)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                val descripcion = etDescripcion.text.toString().trim()
                if (titulo.isNotEmpty()) {
                    val tarea = Tarea(titulo = titulo, descripcion = descripcion)
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.tareaDao().insertarTarea(tarea)
                        val lista = db.tareaDao().obtenerTareas()
                        withContext(Dispatchers.Main) {
                            actualizarLista(lista)
                            binding.recyclerViewTareas.scrollToPosition(0)
                            Toast.makeText(requireContext(), "Tarea agregada", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }

    private fun mostrarDialogoEditarEliminar(tarea: Tarea) {
        val dialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_nueva_tarea, null)
        val etTitulo = dialog.findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = dialog.findViewById<EditText>(R.id.etDescripcion)

        // Cargar datos actuales
        etTitulo.setText(tarea.titulo)
        etDescripcion.setText(tarea.descripcion)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Tarea")
            .setView(dialog)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoTitulo = etTitulo.text.toString().trim()
                val nuevaDescripcion = etDescripcion.text.toString().trim()
                if (nuevoTitulo.isNotEmpty()) {
                    tarea.titulo = nuevoTitulo
                    tarea.descripcion = nuevaDescripcion

                    lifecycleScope.launch(Dispatchers.IO) {
                        db.tareaDao().actualizarTarea(tarea)
                        val lista = db.tareaDao().obtenerTareas()
                        withContext(Dispatchers.Main) {
                            actualizarLista(lista)
                            Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .setNeutralButton("Eliminar") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    db.tareaDao().eliminarTarea(tarea)
                    val lista = db.tareaDao().obtenerTareas()
                    withContext(Dispatchers.Main) {
                        actualizarLista(lista)
                        Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }


    private fun actualizarLista(nuevasTareas: List<Tarea>) {
        adapter.setTareas(nuevasTareas)
    }


}*/

package com.prueba.todotareas

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.prueba.todotareas.data.local.TareasDatabase
import com.prueba.todotareas.data.repository.TareasRepository
import com.prueba.todotareas.databinding.FragmentListaTareasBinding
import kotlinx.coroutines.*

class ListaTareasFragment : Fragment() {

    private lateinit var binding: FragmentListaTareasBinding
    private lateinit var adapter: TareaAdapter
    private lateinit var repository: TareasRepository
    private lateinit var db: TareasDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaTareasBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun ordenarTareasParaVisualizacion(tareas: List<Tarea>): List<Tarea> {
        // No completadas primero, completadas al final
        return tareas.sortedWith(compareBy<Tarea> { it.estado }.thenByDescending { it.id })
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Lista de Tareas"

        db = TareasDatabase.getDatabase(requireContext())
        repository = TareasRepository(requireContext())

        adapter = TareaAdapter(
            mutableListOf(),
            onCheckChanged = { tarea, estado ->
                tarea.estado = estado
                lifecycleScope.launch {
                    guardarTareaActualizada(tarea)
                    // Reordenar en la lista local
                    withContext(Dispatchers.Main) {
                        if (estado) {
                            adapter.moverTareaAlFinal(tarea)
                        } else {
                            adapter.moverTareaAlInicio(tarea)
                        }
                    }
                }
            },
            onItemClick = { tarea ->
                mostrarDialogoEditarEliminar(tarea)
            }
        )

        binding.recyclerViewTareas.adapter = adapter
        binding.recyclerViewTareas.layoutManager = LinearLayoutManager(requireContext())

        binding.progressBar.visibility = View.VISIBLE
        binding.layoutPrincipal.visibility = View.GONE

        lifecycleScope.launch {
            Log.d("DEBUG", "Lanzando carga de tareas...")

            val tareas = try {
                if (hayInternet(requireContext())) {
                    Log.d("DEBUG", "Hay internet, obteniendo y sincronizando tareas...")
                    repository.obtenerTareas() // maneja sincronización y evita duplicados
                } else {
                    Log.d("DEBUG", "Sin internet, usando locales...")
                    db.tareaDao().obtenerTareas()
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "Error al cargar tareas: ${e.message}", e)
                db.tareaDao().obtenerTareas()
            }

            withContext(Dispatchers.Main) {
                Log.d("DEBUG", "Tareas obtenidas: ${tareas.size}")
                adapter.setTareas(tareas)
                binding.progressBar.visibility = View.GONE
                binding.layoutPrincipal.visibility = View.VISIBLE
            }
        }

        binding.fabAgregarTarea.setOnClickListener { mostrarDialogoNuevaTarea() }
        iniciarSincronizacionAutomatica()

    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Lista de Tareas"

        db = TareasDatabase.getDatabase(requireContext())
        repository = TareasRepository(requireContext())

        adapter = TareaAdapter(
            mutableListOf(),
            onCheckChanged = { tarea, estado ->
                tarea.estado = estado
                lifecycleScope.launch {
                    guardarTareaActualizada(tarea)
                    withContext(Dispatchers.Main) {
                        // Refrescar la tarea para que se vea inmediatamente el tachado
                        adapter.actualizarTarea(tarea)
                        // Reordenar según el estado
                        if (estado) {
                            adapter.moverTareaAlFinal(tarea)
                        } else {
                            adapter.moverTareaAlInicio(tarea)
                        }
                    }
                }
            },
            onItemClick = { tarea ->
                mostrarDialogoEditarEliminar(tarea)
            }
        )

        binding.recyclerViewTareas.adapter = adapter
        binding.recyclerViewTareas.layoutManager = LinearLayoutManager(requireContext())

        binding.progressBar.visibility = View.VISIBLE
        binding.layoutPrincipal.visibility = View.GONE

        lifecycleScope.launch {
            val tareas = try {
                if (hayInternet(requireContext())) {
                    repository.obtenerTareas()
                } else {
                    db.tareaDao().obtenerTareas()
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "Error al cargar tareas: ${e.message}", e)
                db.tareaDao().obtenerTareas()
            }

            withContext(Dispatchers.Main) {
                val tareasOrdenadas = ordenarTareasParaVisualizacion(tareas)
                adapter.setTareas(tareasOrdenadas)
                binding.progressBar.visibility = View.GONE
                binding.layoutPrincipal.visibility = View.VISIBLE
            }
        }

        binding.fabAgregarTarea.setOnClickListener { mostrarDialogoNuevaTarea() }
        iniciarSincronizacionAutomatica()
    }

    // ===============================
    // CREAR NUEVA TAREA
    // ===============================
    private fun mostrarDialogoNuevaTarea() {
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nueva_tarea, null)
        val etTitulo = dialog.findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = dialog.findViewById<EditText>(R.id.etDescripcion)

        AlertDialog.Builder(requireContext())
            .setTitle("Nueva Tarea")
            .setView(dialog)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                val descripcion = etDescripcion.text.toString().trim()
                if (titulo.isNotEmpty()) {
                    val nuevaTarea = Tarea(id = 0, titulo = titulo, descripcion = descripcion)
                    lifecycleScope.launch {
                        val exito = repository.crearTarea(nuevaTarea)
                        val mensaje = if (exito) "Tarea guardada correctamente" else "Error al crear tarea"

                        //cargarTareasLocales()
                        // Insertar al inicio de la lista inmediatamente
                        withContext(Dispatchers.Main) {
                            adapter.agregarTarea(nuevaTarea)
                        }
                        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ===============================
    // EDITAR O ELIMINAR TAREA
    // ===============================
    private fun mostrarDialogoEditarEliminar(tarea: Tarea) {
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nueva_tarea, null)
        val etTitulo = dialog.findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = dialog.findViewById<EditText>(R.id.etDescripcion)

        etTitulo.setText(tarea.titulo)
        etDescripcion.setText(tarea.descripcion)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar / Eliminar Tarea")
            .setView(dialog)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoTitulo = etTitulo.text.toString().trim()
                val nuevaDescripcion = etDescripcion.text.toString().trim()
                if (nuevoTitulo.isNotEmpty()) {
                    tarea.titulo = nuevoTitulo
                    tarea.descripcion = nuevaDescripcion

                    lifecycleScope.launch {
                        val online = hayInternet(requireContext())
                        if (online) {
                            repository.actualizarTarea(tarea)
                        }
                        db.tareaDao().actualizarTarea(tarea)
                        //cargarTareasLocales()
                        withContext(Dispatchers.Main) {
                            adapter.actualizarTarea(tarea)
                            // Si cambia estado, mover según corresponda
                            if (tarea.estado) adapter.moverTareaAlFinal(tarea)
                            else adapter.moverTareaAlInicio(tarea)
                        }
                        Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNeutralButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    val online = hayInternet(requireContext())
                    if (online) {
                        repository.eliminarTarea(tarea)
                    }
                    db.tareaDao().eliminarTarea(tarea)
                    //cargarTareasLocales()
                    withContext(Dispatchers.Main) {
                        adapter.eliminarTarea(tarea)
                    }
                    Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ===============================
    // FUNCIONES AUXILIARES
    // ===============================
    private suspend fun cargarTareasLocales() {
        val lista = withContext(Dispatchers.IO) { db.tareaDao().obtenerTareas() }
        withContext(Dispatchers.Main) { adapter.setTareas(lista) }
    }

    private suspend fun guardarTareaActualizada(tarea: Tarea) {
        val online = hayInternet(requireContext())
        if (online) {
            repository.actualizarTarea(tarea)
        }
        db.tareaDao().actualizarTarea(tarea)
    }

    private fun iniciarSincronizacionAutomatica() {
        lifecycleScope.launch {
            while (true) {
                delay(10000L) // cada 10 segundos
                if (hayInternet(requireContext())) {
                    try {
                        val tareasRemotas = repository.obtenerTareas()
                        withContext(Dispatchers.Main) {
                            // Mantener los estados locales
                            val listaActual = adapter.getTareas()
                            tareasRemotas.forEach { tareaRemota ->
                                val local = listaActual.find { it.id == tareaRemota.id }
                                if (local != null) tareaRemota.estado = local.estado
                            }
                            // Ordenar antes de mostrar
                            val ordenada = ordenarTareasParaVisualizacion(tareasRemotas)
                            adapter.setTareas(ordenada)
                        }
                    } catch (e: Exception) {
                        Log.e("SYNC", "Error al sincronizar automáticamente: ${e.message}")
                    }
                }
            }
        }
    }



    private fun hayInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val actNw = cm.getNetworkCapabilities(network) ?: return false
        val conectado = actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

        Log.d("INTERNET", "¿Hay conexión?: $conectado")
        return conectado
    }
}


