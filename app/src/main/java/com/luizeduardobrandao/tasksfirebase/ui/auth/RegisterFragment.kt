package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentRegisterBinding
import com.luizeduardobrandao.tasksfirebase.util.initToolbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!

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
        initToolbar(binding.toolbarRegister)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listeners
    private fun initListeners(){
        // Navegação temporária para acessar HomeFragment ao clicar no botão Criar
        binding.btnRegister.setOnClickListener{
            if (validateData()){
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }

        }
    }

    // Validação dos campos de e-mail e senha (sem uso de MVVM)
    private fun validateData(): Boolean{
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        // 1) valida e-mail
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // e-mail vazio ou inválido
            Toast.makeText(
                requireContext(),
                R.string.text_login_email_error,
                Toast.LENGTH_SHORT
            ).show()
            binding.editTextEmail.text?.clear()
            return false
        }

        // 2) valida senha
        if (password.isEmpty() || password.length < 6) {
            // senha vazia ou muito curta
            Toast.makeText(
                requireContext(),
                R.string.text_login_password_error,
                Toast.LENGTH_SHORT
            ).show()
            binding.editTextPassword.text?.clear()
            return false
        }

        Toast.makeText(requireContext(), R.string.text_create_validated, Toast.LENGTH_SHORT).show()
        return true
    }
}