package com.example.edt.ui.edt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.data.repository.AbbreviationRepository
import com.example.edt.data.repository.EdtRepository
import com.example.edt.util.DateConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EdtViewModel @Inject constructor(
    private val edtRepository: EdtRepository,
    private val abbreviationRepository: AbbreviationRepository,
) : ViewModel() {
    private var _promo: String = ""
    val promo: String get() = _promo

    private lateinit var _date: Date
    val date: Date get() = _date

    private lateinit var _edt: List<CoursEntity>
    val edt: List<CoursEntity> get() = _edt

    private lateinit var _abbreviations: List<AbbreviationEntity>
    val abbreviations: List<AbbreviationEntity> get() = _abbreviations

    init {
        setPromo("but3-ra")
        setDate(Date())
    }
    fun setPromo(promo: String) {
        _promo = promo
    }

    fun setDate(date: Date) {
        _date = DateConverter.previousMonday(date)
    }

    suspend fun refresh() {
        _abbreviations = abbreviationRepository.getAbbreviation()
        _edt = edtRepository.getEdt(promo, date)
    }
}