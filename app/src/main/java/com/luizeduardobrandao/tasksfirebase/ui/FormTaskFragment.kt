package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.data.model.Status
import com.luizeduardobrandao.tasksfirebase.data.model.Task
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentFormTaskBinding
import com.luizeduardobrandao.tasksfirebase.util.initToolbar
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

// Incluir uma nova tarefa

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true // verificação se está criando ou editando uma tarefa
    private var status: Status = Status.TODO

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura a toolbar com título dinâmico
        initToolbarAndTitle()

        // Inicialização da Referências ao RealtimeDatabase e Autenticação do Firebase
        auth = Firebase.auth
        reference = Firebase.database.reference

        // inicialização dos listeners
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Exibe e configura título da toolbar
    private fun initToolbarAndTitle(){
        initToolbar(binding.toolbarForm)
        val title = if (newTask)
            getString(R.string.text_title_toolbar_new_task)
        else
            getString(R.string.text_title_toolbar_edit_task)
        binding.textTitleForm.text = title
    }

    // Configuração dos Listeners
    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            if (validateData()) {
                saveTask()
            }
        }

        binding.rgStatus.setOnCheckedChangeListener { _, id ->
            status = when(id) {
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    // Validação dos dados da tarefa
    private fun validateData(): Boolean {
        val description = binding.editTextDescription.text.toString()

        if (description.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_form_task_error))
            return false
        }

        if (newTask) {         // nova tarefa
            task = Task()
        }

        task.description = description
        task.status = status

        return true
    }

    // Salvar tarefa no Firebase
    private fun saveTask() {
        binding.progressBarForm.visibility = View.VISIBLE

        val userId = auth.currentUser?.uid.orEmpty()
        val tasksRef = reference.child("tasks").child(userId)

        // se for nova tarefa, gera um nó novo; senão, usa o ID existente
        val saveRef = if (newTask) {
            val newTaskRef = tasksRef.push()
            task.id = newTaskRef.key.orEmpty()
            newTaskRef
        } else {
            tasksRef.child(task.id)
        }

        // Salva a tarefa
        saveRef.setValue(task)
            .addOnCompleteListener { result ->
                binding.progressBarForm.visibility = View.GONE

                if (result.isSuccessful) {
                    Toast.makeText(
                        requireContext(), R.string.text_form_task_saved, Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(R.id.action_formTaskFragment_to_homeFragment)
                } else {
                    showBottomSheet(message = getString(R.string.text_generic_error))
                }
            }
    }
}