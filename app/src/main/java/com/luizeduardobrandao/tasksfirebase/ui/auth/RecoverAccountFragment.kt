package com.luizeduardobrandao.tasksfirebase.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentRecoverAccountBinding
import com.luizeduardobrandao.tasksfirebase.util.initToolbar
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

class RecoverAccountFragment : Fragment() {

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
        initToolbar(binding.toolbarRecover)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners(){
        binding.btnRecover.setOnClickListener {
            if(validateData()){
                findNavController().navigate(R.id.action_recoverAccountFragment_to_loginFragment)
            }

        }
    }

    private fun validateData(): Boolean {
        val email = binding.editTextEmail.text.toString().trim()

        // 1) valida e-mail
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // e-mail vazio ou inválido
            showBottomSheet(message = getString(R.string.text_login_email_error))
            binding.editTextEmail.text?.clear()
            return false
        }

        Toast.makeText(requireContext(), R.string.text_email_sent, Toast.LENGTH_SHORT).show()
        return true
    }

}