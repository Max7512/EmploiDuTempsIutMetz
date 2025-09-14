package com.iutmetz.edt.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint // cette annotation permet de rendre la classe injectable
abstract class BaseFragment : Fragment() { // cette classe permet de définir des fonctions et des objets qui seront partagés par les fragments

    private val vibrationDurationMillis: Long = 1000 // on définit la durée de vibration en millisecondes

    @Inject
    lateinit var vibrator: Vibrator // on injecte un objet Vibrator qui sera utilisé pour la fonction vibrate

    private fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) } // cette fonction permet de ne pas naviguer vers un fragment déjà ouvert
    }

    protected fun navigate(direction: NavDirections) { // cette fonction permet de naviguer vers un fragment
        findNavController().safeNavigate(direction) // on utilise la fonction safeNavigate pour naviguer vers un fragment sans erreur
    }

    @SuppressWarnings("deprecation")
    protected fun vibrate() { // cette fonction permet de jouer une vibration sur le téléphone
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            vibrator.vibrate(vibrationDurationMillis)
        } else {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    vibrationDurationMillis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }

    protected fun showProgressIndicator(show: Boolean) { // cette fonction permet d'afficher ou masquer un indicateur de chargement
        if (activity is MainActivity) {
            (activity as MainActivity).showProgressIndicator(show)
        }
    }

    fun showMessage(message: String?) { // cette fonction permet d'afficher un message en bas de l'écran à l'utilisateur
        if (message == null) return

        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() { // cette fonction est appelée lorsque le fragment est détruit
        showProgressIndicator(false)
        super.onStop()
    }
}