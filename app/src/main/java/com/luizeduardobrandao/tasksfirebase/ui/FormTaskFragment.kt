package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.data.model.Status
import com.luizeduardobrandao.tasksfirebase.data.model.Task
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentFormTaskBinding
import com.luizeduardobrandao.tasksfirebase.util.FirebaseHelper
import com.luizeduardobrandao.tasksfirebase.util.initToolbar
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

// Incluir uma nova tarefa

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true // verificação se está criando ou editando uma tarefa
    private var status: Status = Status.TODO

    // Recuperando argumentos de TodoFragment (HomeFragment)
    private val args: FormTaskFragmentArgs by navArgs()

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
        initToolbar(binding.toolbarForm)

        // Recupera as informações do argumento
        getArgs()

        // inicialização dos listeners
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    // Função para recuperar argumento passado
    private fun getArgs(){
        // ".let" só acessa a tarefa se for diferente de nulo
        args.task.let {
            if (it != null) {
                this.task = it

                configTask()
            }
        }
    }

    // Popula o FormTask com as informações recebidas
    private fun configTask(){
        newTask = false
        status = task.status
        binding.textTitleForm.setText(R.string.text_title_toolbar_edit_task)

        binding.editTextDescription.setText(task.description)
        binding.rgStatus.check(
            when (task.status) {
                Status.TODO -> R.id.rbTodo
                Status.DOING -> R.id.rbDoing
                else -> R.id.rbDone
            }
        )
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

        val userId = FirebaseHelper.getIdUser()
        val tasksRef = FirebaseHelper.getDatabase().child("tasks").child(userId)

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

                    // escolhe a mensagem de acordo com newTask
                    val messageRes = if (newTask){
                        R.string.text_form_task_saved
                    } else {
                        R.string.text_form_task_update
                    }

                    Toast.makeText(
                        requireContext(), messageRes, Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(R.id.action_formTaskFragment_to_homeFragment)
                } else {
                    showBottomSheet(message = getString(R.string.text_generic_error))
                }
            }
    }
}