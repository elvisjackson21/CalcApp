package com.example.calculatorapp

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.objecthunter.exp4j.ExpressionBuilder


class AppViewModel: ViewModel() {

    private val _firstNumber: MutableStateFlow<String?> = MutableStateFlow(null)
    val firstNumber = _firstNumber.asStateFlow()

    private val _action: MutableStateFlow<String> = MutableStateFlow("")

    fun setFirstNumber(input: String?) {

        _firstNumber.update { input }

    }

    fun setAction(action: String) {

        _action.update { action }

    }

    fun resetAll() {

        _action.update { "" }
        _firstNumber.update { null }

    }

    fun getResult(): Double {

        val value = _firstNumber.value!!

        return ExpressionBuilder(value).build().evaluate()

    }
}



