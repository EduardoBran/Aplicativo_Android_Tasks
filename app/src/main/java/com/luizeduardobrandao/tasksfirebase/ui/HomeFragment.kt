package com.luizeduardobrandao.tasksfirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebase.R
import com.luizeduardobrandao.tasksfirebase.databinding.FragmentHomeBinding
import com.luizeduardobrandao.tasksfirebase.ui.adapter.ViewPageAdapter
import com.luizeduardobrandao.tasksfirebase.util.showBottomSheet

// Exibe todas as tarefas

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Logout
        initLogoutButton()

        // TabLayout
        initTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Logout do usuário
    private fun initLogoutButton() {
        binding.btnLogout.setOnClickListener {
            showBottomSheet(
                titleButton = R.string.text_button_dialog_confirm_logout,
                titleDialog = R.string.text_title_dialog_confirm_logout,
                message = getString(R.string.text_message_dialog_confirm_logout),
                onClick = {
                    auth.signOut() // desconecta do Firebase
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    Toast.makeText(
                        requireContext(), R.string.text_toast_confirm_logout, Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun initTabLayout() {
        val pageAdapter = ViewPageAdapter(requireActivity())

        // Alterar o componente dinamicamente
        binding.viewPager.adapter = pageAdapter

        // Adiciona os títulos
        pageAdapter.addFragment(TodoFragment(), R.string.status_task_todo)
        pageAdapter.addFragment(DoingFragment(), R.string.status_task_doing)
        pageAdapter.addFragment(DoneFragment(), R.string.status_task_done)

        // Adiciona o limite de quantos fragments tem no TabLayout
        binding.viewPager.offscreenPageLimit = pageAdapter.itemCount

        // Associa o ViewPager2 ao TabLayout
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            // Setar o título
            tab.text = getString(pageAdapter.getTitle(position))
        }.attach()
    }
}