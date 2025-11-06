package com.prueba.todotareas.data.remote

import com.prueba.todotareas.Tarea
import retrofit2.Response
import retrofit2.http.*

interface TareaApi {

    @GET("tasks/")
    suspend fun obtenerTareas(): Response<List<Tarea>>

    @POST("tasks/")
    suspend fun crearTarea(@Body tarea: Tarea): Response<Tarea>

    @PUT("tasks/{id}")
    suspend fun actualizarTarea(@Path("id") id: Int, @Body tarea: Tarea): Response<Tarea>

    @DELETE("tasks/{id}")
    suspend fun eliminarTarea(@Path("id") id: Int): Response<Unit>
}
