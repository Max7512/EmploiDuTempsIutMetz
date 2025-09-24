package com.iutmetz.edt.ui.edt.parametre

import android.view.LayoutInflater
import com.iutmetz.edt.data.local.entity.SessionEntity
import com.iutmetz.edt.databinding.LayoutParametreCouleursBinding

class ParametreCouleurs(
    session: SessionEntity,
    private val inflater: LayoutInflater
): Parametre(session) {
    override val binding = LayoutParametreCouleursBinding.inflate(inflater)

    override fun initView() {
        binding.apply {
            ibCours.setOnClickListener {

            }
        }
    }

    fun colorPicker(onPicked: () -> Unit) {

        onPicked()
    }
}