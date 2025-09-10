package com.example.edt.ui.edt

import androidx.lifecycle.ViewModel
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.data.repository.AbbreviationRepository
import com.example.edt.data.repository.EdtRepository
import com.example.edt.ui.edt.affichage.Affichage
import com.example.edt.util.DateConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EdtViewModel @Inject constructor(
    private val edtRepository: EdtRepository,
    private val abbreviationRepository: AbbreviationRepository,
) : ViewModel() {
    private var _promo: String = ""
    var promo: String get() = _promo
        set(value) {
            _promo = value
        }

    private lateinit var _date: Date
    var date: Date get() = _date
        set(value) {
            _date = DateConverter.previousMonday(value)
        }

    private lateinit var _edt: List<CoursEntity>
    val edt: List<CoursEntity> get() = _edt

    private lateinit var _abbreviations: List<AbbreviationEntity>
    val abbreviations: List<AbbreviationEntity> get() = _abbreviations

    init {
        date = Date()
    }
    suspend fun refresh(affichage: Affichage) {
        _abbreviations = abbreviationRepository.getAbbreviation()
        _edt = edtRepository.getEdt(promo, date)

        affichage.afficher(edt, abbreviations)
    }
}