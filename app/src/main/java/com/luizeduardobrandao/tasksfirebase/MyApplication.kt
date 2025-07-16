package com.luizeduardobrandao.tasksfirebase

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance()
            .installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
    }
}

// * Por que isso resolve?
// - Gera um token “debug” que o Firebase reconhece como válido, mesmo sem usar SafetyNet
//   ou Play Integrity.
//
// - Permite testar sendPasswordResetEmail() em simulador ou dispositivo sem ver erros de
//   “No AppCheckProvider installed”.
//
// - Em produção, basta trocar o provedor (Play Integrity ou SafetyNet) no mesmo lugar, sem mudar
//   o restante do código.