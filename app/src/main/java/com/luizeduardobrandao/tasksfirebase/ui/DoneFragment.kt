package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.data.model.Status
import com.luizeduardobrandao.tasksfirebase.data.model.Task
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentDoneBinding
import com.luizeduardobrandao.tasksfirebase.ui.adapter.TaskAdapter

// Exibe as tarefas completadas

class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        getTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // RecyclerView
    private fun initRecyclerView(){

        taskAdapter = TaskAdapter { task, option ->
            optionSelected(task, option)
        }

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }

//        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvTasks.setHasFixedSize(true)
//        binding.rvTasks.adapter = taskAdapter
    }

    // Configuração dos eventos de cliques da RecyclerView
    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                Toast.makeText(requireContext(), R.string.text_remove, Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_EDIT -> {
                Toast.makeText(requireContext(), "Editando", Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes", Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_BACK -> {
                Toast.makeText(requireContext(), "Voltar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Retornar a Lista de Tarefas (posteriormente será usado para recupear a lista do Firebase)
    private fun getTasks() {
        val taskList = listOf(
            // Criando Lista Temporária para visualização das Tarefas
            Task("0", "Ble ble ble", Status.DONE),
            Task("1", "Validar informações na tela de login", Status.DONE),
            Task("2", "Adicionar nova funcionalidade no app", Status.DONE),
            Task("3", "Salvar token localmente", Status.DONE),
            Task("4", "Criar funcionalidade de layout no app", Status.DONE),
            Task("5", "Configurar Firebase", Status.DONE),
            Task("6", "Posteriormente configurar o Room", Status.DONE)
        )

        taskAdapter.submitList(taskList)
    }
}