package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentRecoverAccountBinding
import com.luizeduardobrandao.tasksfirebase.ui.BaseFragment
import com.luizeduardobrandao.tasksfirebase.util.FirebaseHelper
import com.luizeduardobrandao.tasksfirebase.util.initToolbar
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding: FragmentRecoverAccountBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura a toolbar
        initToolbar(binding.toolbarRecover)

        // Configura listeners
        initListeners()
    }

    // Configura o listener do botão de recuperação.
    private fun initListeners(){
        binding.btnRecover.setOnClickListener {
            binding.btnRecover.setOnClickListener {
                val email = binding.editTextEmail.text.toString().trim()

                if (!validateData(email)) return@setOnClickListener

                recoverPassword(email)
            }
        }
    }

    // Valida o formato do e-mail.
    private fun validateData(email: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showBottomSheet(message = getString(R.string.text_login_email_error))
            binding.editTextEmail.text?.clear()
            return false
        }

        // função para esconder teclado extendida de BaseFragment
        hideKeyboard()

        return true
    }

    // Envia o e-mail de recuperação de senha pelo Firebase e trata o resultado.
    private fun recoverPassword(email: String) {

        // Exibe a progress bar
        binding.progressBarRecover.isVisible = true

        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                // Esconde progress bar
                binding.progressBarRecover.isVisible = false

                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(), R.string.text_email_sent, Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(
                        R.id.action_recoverAccountFragment_to_loginFragment
                    )
                } else {
                    // Exibe mensagem de erro do Firebase ou genérica
                    val message = task.exception
                        ?.localizedMessage
                        ?: getString(R.string.text_generic_error)
                    showBottomSheet(message = message)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}