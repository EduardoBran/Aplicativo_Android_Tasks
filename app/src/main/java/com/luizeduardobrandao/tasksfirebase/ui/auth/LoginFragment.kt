package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
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
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentLoginBinding
import com.luizeduardobrandao.tasksfirebase.util.FirebaseHelper
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialização da Autenticação do Firebase
        auth = Firebase.auth

        // Inicaliza os liteners
        initListeners()
    }

    // Sobrescrevendo mét0d0 onStart para verificação de usuário já autenticado
    override fun onStart() {
        super.onStart()

        // Se já houver um user logado, manda direto para Home
        auth.currentUser?.let {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            if (!validateData(email, password)) return@setOnClickListener
            loginUser(email, password)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    // Valida e-mail e senha localmente.
    private fun validateData(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showBottomSheet(message = getString(R.string.text_login_email_error))
            binding.editTextEmail.text?.clear()
            return false
        }
        if (password.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_login_password_error))
            binding.editTextPassword.text?.clear()
            return false
        }
        return true
    }

    // Realiza o login no Firebase e navega para Home em caso de sucesso.
    private fun loginUser(email: String, password: String) {

        // Exibe a progress bar
        binding.progressBarLogin.isVisible = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                // Esconde progress bar
                binding.progressBarLogin.isVisible = false

                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.text_login_validated,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navega usando a action que limpa o back‑stack (definida no nav_graph)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    // Mostra mensagem de erro do Firebase ou genérica
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