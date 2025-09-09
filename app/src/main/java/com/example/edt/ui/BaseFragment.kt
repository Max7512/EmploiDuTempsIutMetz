package com.example.edt.ui

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

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    private val vibrationDurationMillis: Long = 1000

    @Inject
    lateinit var vibrator: Vibrator

    private fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
    }

    protected fun navigate(direction: NavDirections) {
        findNavController().safeNavigate(direction)
    }

    @SuppressWarnings("deprecation")
    protected fun vibrate() {
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

    protected fun showProgressIndicator(show: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).showProgressIndicator(show)
        }
    }

    fun showMessage(message: String?) {
        if (message == null) return

        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        showProgressIndicator(false)
        super.onStop()
    }
}