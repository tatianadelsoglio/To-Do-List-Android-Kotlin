package com.prueba.todotareas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")

data class Tarea(
    @PrimaryKey val id: Int,
    var titulo: String,
    var descripcion: String,
    var estado: Boolean = false
)
