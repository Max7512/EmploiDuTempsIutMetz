package com.iutmetz.edt.ui.parametres.parametre

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import com.iutmetz.edt.databinding.LayoutParametreSupportBinding
import androidx.core.net.toUri

class ParametreSupport(
    private val gitUrl: String,
    private val context: Context,
    inflater: LayoutInflater,
    parent: ViewGroup
) : Parametre() {
    override val binding: LayoutParametreSupportBinding = LayoutParametreSupportBinding.inflate(inflater, parent, true)

    override fun initView() {
        binding.apply {
            ibBug.setOnClickListener {
                renvoieGit("/issues")
            }

            ibDiscussion.setOnClickListener {
                renvoieGit("/discussions")
            }

            ibGitReleases.setOnClickListener {
                renvoieGit("/releases")
            }
        }
    }

    fun renvoieGit(url: String = "") {
        val browserIntent = Intent(Intent.ACTION_VIEW, (gitUrl + url).toUri())
        startActivity(context, browserIntent, null)
    }
}