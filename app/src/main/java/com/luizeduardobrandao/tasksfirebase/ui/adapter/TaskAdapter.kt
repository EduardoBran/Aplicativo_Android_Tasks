package com.luizeduardobrandao.tasksfirebase.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.tasksfirebase.data.model.Status
import com.luizeduardobrandao.tasksfirebase.data.model.Task
import com.luizeduardobrandao.tasksfirebase.databinding.ItemTaskBinding

// * Adapter para exibir uma lista de Task em um RecyclerView.
// * @param taskList lista de objetos Task a serem renderizados

class TaskAdapter(
    private val taskList: List<Task>
): RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    // ViewHolder interno que mantém referência ao binding gerado pelo layout item_task.xml
    inner class MyViewHolder(
        val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // Infla o layout do item e cria um ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    // Retorna a quantidade de itens na lista
    override fun getItemCount() = taskList.size

    // Vincula os dados da Task à view correspondente no ViewHolder.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Obtém a Task na posição atual
        val task = taskList[position]

        // Define o texto da descrição
        holder.binding.textDescription.text = task.description

        // Exibir indicadores
        setIndicators(task, holder)
    }

    // Função para exibição dos indicadores (seta de voltar e avançar)
    private fun setIndicators(task: Task, holder: MyViewHolder) {
        when (task.status){
            Status.TODO -> {
                holder.binding.btnNext.isVisible = true
            }

            Status.DOING -> {
                holder.binding.btnBack.isVisible = true
                holder.binding.btnNext.isVisible = true
            }

            Status.DONE -> {
                holder.binding.btnBack.isVisible = true
            }
        }
    }
}