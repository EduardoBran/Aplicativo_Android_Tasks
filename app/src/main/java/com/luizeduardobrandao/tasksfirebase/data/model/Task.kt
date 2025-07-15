package com.luizeduardobrandao.tasksfirebase.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// * Data class que representa uma tarefa vinda do Firebase.
// * Implementa Parcelable para permitir passagem através de Bundles.

@Parcelize
data class Task(
    // id do Firebase espera uma String
    val id: String,
    val description: String,
    val status: Status
) : Parcelable
