package com.prueba.todotareas.data.repository

import android.content.Context
import android.util.Log
import com.prueba.todotareas.Tarea
import com.prueba.todotareas.data.local.TareasDatabase
import com.prueba.todotareas.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TareasRepository(context: Context) {

    private val db = TareasDatabase.getDatabase(context)
    private val api = RetrofitClient.api

    companion object {
        private const val TAG = "TareasRepository"
    }

    // ===============================
    // OBTENER TODAS LAS TAREAS
    // ===============================
    suspend fun obtenerTareas(): List<Tarea> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.obtenerTareas()
                if (response.isSuccessful) {
                    val tareasRemotas = response.body() ?: emptyList()
                    Log.d(TAG, "Tareas remotas recibidas: ${tareasRemotas.size}")

                    // Sincronizar la base local con el backend
                    db.tareaDao().insertarTareas(tareasRemotas)
                    tareasRemotas
                } else {
                    Log.e(TAG, "Error al obtener tareas remotas: ${response.code()}")
                    db.tareaDao().obtenerTareas()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fallo en obtenerTareas(): ${e.message}", e)
                db.tareaDao().obtenerTareas()
            }
        }
    }

    // ===============================
    // CREAR TAREA
    // ===============================
    suspend fun crearTarea(tarea: Tarea): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.crearTarea(tarea)
                if (response.isSuccessful) {
                    val tareaRemota = response.body()
                    if (tareaRemota != null) {
                        db.tareaDao().insertarTarea(tareaRemota)
                        Log.d(TAG, "Tarea creada y guardada localmente con ID ${tareaRemota.id}")
                        return@withContext true
                    }
                }
                Log.e(TAG, "Error al crear tarea: ${response.code()}")
                false
            } catch (e: Exception) {
                Log.e(TAG, "Error de red al crear tarea: ${e.message}")
                false
            }
        }
    }

    // ===============================
    // ACTUALIZAR TAREA
    // ===============================
    suspend fun actualizarTarea(tarea: Tarea): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.actualizarTarea(tarea.id, tarea)
                if (response.isSuccessful) {
                    db.tareaDao().actualizarTarea(tarea)
                    Log.d(TAG, "Tarea actualizada correctamente (ID ${tarea.id})")
                    true
                } else {
                    Log.e(TAG, "Error al actualizar tarea: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error de red al actualizar tarea: ${e.message}")
                false
            }
        }
    }

    // ===============================
    // ELIMINAR TAREA
    // ===============================
    suspend fun eliminarTarea(tarea: Tarea): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.eliminarTarea(tarea.id)
                if (response.isSuccessful) {
                    db.tareaDao().eliminarTarea(tarea)
                    Log.d(TAG, "Tarea eliminada correctamente (ID ${tarea.id})")
                    true
                } else {
                    Log.e(TAG, "Error al eliminar tarea: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error de red al eliminar tarea: ${e.message}")
                false
            }
        }
    }
}
