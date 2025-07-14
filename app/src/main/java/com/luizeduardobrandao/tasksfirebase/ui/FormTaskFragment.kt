package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentFormTaskBinding
import com.luizeduardobrandao.tasksfirebase.util.initToolbar

// Incluir uma nova tarefa

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

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
        initToolbar(binding.toolbarForm)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            if (validateData()){
                findNavController().navigate(R.id.action_formTaskFragment_to_homeFragment)
            }
        }
    }

    private fun validateData(): Boolean {
        val description = binding.editTextDescription.text.toString().trim()

        if (description.isEmpty()){
            Toast.makeText(
                requireContext(),
                R.string.text_form_task_error,
                Toast.LENGTH_SHORT
            ).show()
            binding.editTextDescription.text?.clear()
            return false
        }

        Toast.makeText(requireContext(), R.string.text_form_task_saved, Toast.LENGTH_SHORT).show()
        return true
    }

}