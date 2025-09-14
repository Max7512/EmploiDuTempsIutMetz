package com.iutmetz.edt.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.iutmetz.edt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // cette annotation permet de rendre la classe injectable
class MainActivity : AppCompatActivity() { // cette classe permet d'afficher la vue de l'application et de gérer les interactions avec l'utilisateur
    private lateinit var binding: ActivityMainBinding // on utilise un binding pour accéder aux éléments de la vue

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) { // cette fonction est appelée lorsque l'activité est créée
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // on initialise la vue
        val view = binding.root // on récupère la vue
        setContentView(view) // on affiche la vue

        if (Build.VERSION.SDK_INT >= 33) { // en fonction de la version de l'appareil, on gère les interactions avec l'utilisateur de manière différente
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                finishActivity()
            }
        } else {
            onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishActivity()
                }
            })
        }
    }

    private fun finishActivity() { // cette fonction permet de terminer l'activité
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q && isTaskRoot &&
            (supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.backStackEntryCount
                ?: 0) == 0 &&
            supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        }
    }

    fun showProgressIndicator(show: Boolean) { // cette fonction permet d'afficher ou masquer l'indicateur de chargement
        binding.progressLayout.visibility = if (show) View.VISIBLE else View.GONE
    }
}