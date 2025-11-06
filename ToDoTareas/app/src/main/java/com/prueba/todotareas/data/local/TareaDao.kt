package com.prueba.todotareas.data.local

import androidx.room.*
import com.prueba.todotareas.Tarea

@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas ORDER BY estado ASC, id DESC")
    suspend fun obtenerTareas(): List<Tarea>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: Tarea)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTareas(tareas: List<Tarea>)


    @Update
    suspend fun actualizarTarea(tarea: Tarea)

    @Delete
    suspend fun eliminarTarea(tarea: Tarea)


}
