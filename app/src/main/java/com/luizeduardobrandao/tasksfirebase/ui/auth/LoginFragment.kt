package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentLoginBinding
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

    // Listeners
    private fun initListeners(){

        // Navegação temporária para acessar HomeFragment ao clicar no botão Login
        binding.btnLogin.setOnClickListener {
            if (validateData()) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    // Validação dos campos de e-mail e senha (sem uso de MVVM)
    private fun validateData(): Boolean{
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        // 1) valida e-mail
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // e-mail vazio ou inválido
            showBottomSheet(message = getString(R.string.text_login_email_error))
            binding.editTextEmail.text?.clear()
            return false
        }

        // 2) valida senha
        if (password.isEmpty() || password.length < 6) {
            // senha vazia ou muito curta
            showBottomSheet(message = getString(R.string.text_login_password_error))
            binding.editTextPassword.text?.clear()
            return false
        }

        Toast.makeText(requireContext(), R.string.text_login_validated, Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}