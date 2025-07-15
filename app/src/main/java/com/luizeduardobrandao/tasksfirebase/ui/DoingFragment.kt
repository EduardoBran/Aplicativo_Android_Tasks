package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.data.model.Status
import com.luizeduardobrandao.tasksfirebase.data.model.Task
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentDoingBinding
import com.luizeduardobrandao.tasksfirebase.ui.adapter.TaskAdapter

// Exibe tarefas realizadas no momento

class DoingFragment : Fragment() {

    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(getTasks())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // RecyclerView
    private fun initRecyclerView(taskList: List<Task>){

        taskAdapter = TaskAdapter(taskList)

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.setHasFixedSize(true)
        binding.rvTasks.adapter = taskAdapter
    }

    // Retornar a Lista de Tarefas (posteriormente será usado para recupear a lista do Firebase)
    private fun getTasks() = listOf<Task>(
        // Criando Lista Temporária para visualização das Tarefas
        Task("0", "Bla bla bla", Status.DOING),
        Task("1", "Validar informações na tela de login", Status.DOING),
        Task("2", "Adicionar nova funcionalidade no app", Status.DOING),
        Task("3", "Salvar token localmente", Status.DOING),
        Task("4", "Criar funcionalidade de layout no app", Status.DOING),
        Task("5", "Configurar Firebase", Status.DOING),
        Task("6", "Posteriormente configurar o Room", Status.DOING)
    )
}