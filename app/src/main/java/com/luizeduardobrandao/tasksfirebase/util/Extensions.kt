package com.luizeduardobrandao.tasksfirebase.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// * Função para inicialização da Toolbar

fun Fragment.initToolbar(toolbar: Toolbar) {
    val activity = requireActivity() as AppCompatActivity

    activity.setSupportActionBar(toolbar)
    activity.title = ""
    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener {
        findNavController().navigateUp()
    }
}
