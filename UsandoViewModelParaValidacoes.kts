
** LoginViewModel

package com.luizeduardobrandao.tasksfirebase.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebase.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Entradas do usuário
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    // Erros inline
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError
    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    // Evento único de login
    private val _loginEvent = MutableSharedFlow<Unit>(replay = 0)
    val loginEvent: SharedFlow<Unit> = _loginEvent

    fun onLoginClicked() {
        val e = email.value
        val p = password.value
        var valid = true

        // valida e-mail
        if (e.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            _emailError.value = null
            _emailError.value = getApplication<Application>()
                .getString(R.string.text_login_email_error)
            valid = false
        } else {
            _emailError.value = null
        }

        // valida senha
        if (p.length < 6) {
            // força emissão dupla: primeiro limpa...
            _passwordError.value = null
            _passwordError.value = getApplication<Application>()
                .getString(R.string.text_login_password_error)
            valid = false
        } else {
            _passwordError.value = null
        }

        if (!valid) return

        // dispara evento de login
        viewModelScope.launch {
            _loginEvent.emit(Unit)
        }
    }
}





** LoginFragment


package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentLoginBinding
import com.luizeduardobrandao.tasksfirebase.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

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

        initListeners()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listeners
    private fun initListeners(){

        // Atualiza ViewModel enquanto o usuário digita
        binding.editTextEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.email.value = text.toString()
        }
        binding.editTextPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.password.value = text.toString()
        }

        // Navegação temporária para acessar HomeFragment ao clicar no botão Login
        binding.btnLogin.setOnClickListener {
            viewModel.onLoginClicked()
        }

        // Navega para Criação de Conta
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Navega para Recuperação de Conta
        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    // Observa StateFlows e SharedFlows do ViewModel
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Erro de e-mail
                launch {
                    viewModel.emailError.collect { error ->
                        if (error != null){
                            // limpa o campo e mostra mensagem inline
                            binding.editTextEmail.text?.clear()
                            binding.editTextEmail.error = error
                        } else {
                            binding.editTextEmail.error = null
                        }
                    }
                }

                // Erro de senha
                launch {
                    viewModel.passwordError.collect { error ->
                        if (error != null) {
                            binding.editTextPassword.text?.clear()
                            binding.editTextPassword.error = error
                        } else {
                            binding.editTextPassword.error = null
                        }
                    }
                }

                // Evento único de login bem-sucedido
                launch {
                    viewModel.loginEvent.collect {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.text_login_validated),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }
    }
}
