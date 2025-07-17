package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentRegisterBinding
import com.luizeduardobrandao.tasksfirebase.util.FirebaseHelper
import com.luizeduardobrandao.tasksfirebase.util.initToolbar
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa Toolbar
        initToolbar(binding.toolbarRegister)

        // Inicializa Firebase Auth
        auth = Firebase.auth

        // Inicializa os listeners
        initListeners()
    }

    // 1) Inicializa os listeners, sem navegar imediatamente
    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            // pega os textos uma única vez
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            // se não validar, retorna e não chama o Firebase ainda
            if (!validateData(email, password)) return@setOnClickListener

            // aqui já sabemos que email e senha são válidos
            registerUser(email, password)
        }
    }

    // 2) Apenas valida; não faz cadastro nem navega
    private fun validateData(email: String, password: String): Boolean {

        // Valida e-mail
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showBottomSheet(message = getString(R.string.text_login_email_error))
            binding.editTextEmail.text?.clear()
            return false
        }

        // Valida senha
        if (password.isEmpty() || password.length < 6) {
            showBottomSheet(message = getString(R.string.text_password_min_length_error))
            binding.editTextPassword.text?.clear()
            return false
        }
        return true
    }

    // 3) Chama o Firebase e só navega em caso de sucesso
    private fun registerUser(email: String, password: String) {

        // Exibe o ProgressBar
        binding.progressBarRegister.isVisible = true

        // Realiza a criação do usuário
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBarRegister.isVisible = false

                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(), R.string.text_create_validated, Toast.LENGTH_SHORT
                    ).show()

                    // navega somente depois de criado
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    // pega mensagem de erro do Firebase e se não encontrar, exibe a minha.
//                    val message = task.exception
//                        ?.localizedMessage
//                        ?: getString(R.string.text_generic_error)
//                    showBottomSheet(message = message)

                    showBottomSheet(
                        message =
                            getString(
                                FirebaseHelper.validError(task.exception?.message.toString())
                            )
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}