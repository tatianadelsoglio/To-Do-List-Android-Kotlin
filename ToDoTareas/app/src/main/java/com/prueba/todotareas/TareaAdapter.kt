package com.prueba.todotareas

import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prueba.todotareas.databinding.ItemTareaBinding


class TareaAdapter(
    private var tareas: MutableList<Tarea>,
    private val onCheckChanged: (Tarea, Boolean) -> Unit,
    private val onItemClick: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    inner class TareaViewHolder(val binding: ItemTareaBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(tarea: Tarea) {
            with(binding) {
                tvTitulo.text = tarea.titulo
                tvDescripcion.text = tarea.descripcion
                tvDescripcion.maxLines = 1
                tvDescripcion.ellipsize = TextUtils.TruncateAt.END

                // Colores y tachado
                if (tarea.estado) {
                    tvTitulo.paintFlags = tvTitulo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvTitulo.setTextColor(Color.GRAY)
                    tvDescripcion.setTextColor(Color.GRAY)
                } else {
                    tvTitulo.paintFlags = tvTitulo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    tvTitulo.setTextColor(Color.BLACK)
                    tvDescripcion.setTextColor(Color.DKGRAY)
                }

                cbEstado.setOnCheckedChangeListener(null)
                cbEstado.isChecked = tarea.estado
                cbEstado.setOnCheckedChangeListener { _, isChecked ->
                    tarea.estado = isChecked
                    onCheckChanged(tarea, isChecked)
                }

                // Click sobre el item para editar/eliminar o leer descripción completa
                root.setOnClickListener { onItemClick(tarea) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.bind(tareas[position])
    }


    override fun getItemCount() = tareas.size

    fun setTareas(nuevasTareas: List<Tarea>) {
        tareas = nuevasTareas.toMutableList()
        notifyDataSetChanged()
    }

    fun agregarTarea(tarea: Tarea) {
        tareas.add(0, tarea)
        notifyItemInserted(0)
    }

    fun actualizarTarea(tarea: Tarea) {
        val index = tareas.indexOfFirst { it.id == tarea.id }
        if (index != -1) {
            tareas[index] = tarea
            notifyItemChanged(index)
        }
    }

    fun eliminarTarea(tarea: Tarea) {
        val index = tareas.indexOfFirst { it.id == tarea.id }
        if (index != -1) {
            tareas.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getTareas(): List<Tarea> = tareas.toList()


    // Funciones para mover tareas
    fun moverTareaAlFinal(tarea: Tarea) {
        val index = tareas.indexOf(tarea)
        if (index != -1 && index != tareas.size - 1) {
            tareas.removeAt(index)
            tareas.add(tarea)
            notifyItemMoved(index, tareas.size - 1)
        }
    }

    fun moverTareaAlInicio(tarea: Tarea) {
        val index = tareas.indexOf(tarea)
        if (index != -1 && index != 0) {
            tareas.removeAt(index)
            tareas.add(0, tarea)
            notifyItemMoved(index, 0)
        }
    }



}
