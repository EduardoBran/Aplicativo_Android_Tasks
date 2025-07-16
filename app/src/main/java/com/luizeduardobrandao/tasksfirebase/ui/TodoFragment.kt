package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.data.model.Status
import com.luizeduardobrandao.tasksfirebase.data.model.Task
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentTodoBinding
import com.luizeduardobrandao.tasksfirebase.ui.adapter.TaskAdapter
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

// Exibe tarefas pendentes

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialização da Referências ao RealtimeDatabase e Autenticação do Firebase
        auth = Firebase.auth
        reference = Firebase.database.reference

        // Inicializa os Listeners
        initListeners()

        // Inicializa Recycler View
        initRecyclerView()

        getTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listeners
    private fun initListeners() {

        // Botão Float
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
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
            TaskAdapter.SELECT_NEXT -> {
                Toast.makeText(requireContext(), "Next", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_REMOVE -> {
                showBottomSheet(
                    titleDialog = R.string.text_title_dialog_confirm_remove,
                    message = getString(R.string.text_message_dialog_confirm_remove),
                    titleButton = R.string.text_button_dialog_confirm_logout,
                    onClick = {
                        deleteTask(task)
                    }
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                Toast.makeText(requireContext(), "Editando", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Verifica se a lista é vazia
    private fun listEmpty(taskList: List<Task>) {
        binding.textInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_list_task_empty)
        } else {
            ""
        }
    }

    // Retornar a Lista de Tarefas do Firebase
    private fun getTasks() {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid.orEmpty())
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val taskList = mutableListOf<Task>()
                    for (ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task

                        // verificando status antes de adicionar
                        if (task.status == Status.TODO) {
                            taskList.add(task)
                        }
                    }

                    // Oculta progress bar e chama função de lista vazia
                    binding.progressBar.isVisible = false
                    listEmpty(taskList)

                    // Ordena para nova tarefa adicionada ser a primeira da lista
                    taskList.reverse()

                    // Atualiza a Lista
                    taskAdapter.submitList(taskList)
                }

                // Cancela a busca (aplicativo fechado ou pausado)
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(), R.string.text_generic_error, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Deletando uma tarefa
    private fun deleteTask(task: Task){
        reference
            .child("tasks")
            .child(auth.currentUser?.uid.orEmpty())
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful){
                    Toast.makeText(
                        requireContext(), R.string.text_remove, Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(), R.string.text_generic_error, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}