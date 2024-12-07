package com.example.constraintlayout

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.constraintlayout.R
import java.util.*

class MainActivity : AppCompatActivity(), TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtValue: EditText
    private lateinit var edtPeople: EditText
    private lateinit var textValue: TextView
    private var ttsSucess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtValue = findViewById(R.id.edtValue)
        edtValue.addTextChangedListener(this)

        tts = TextToSpeech(this, this)

        edtPeople = findViewById(R.id.edtPeople)
        edtPeople.addTextChangedListener(this)

        textValue = findViewById(R.id.textValue)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("PDM24", "Antes de mudar")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24", "Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d("PDM24", "Depois de mudar")
        try {
            val valor = s.toString().toDouble()
            Log.d("PDM24", "Valor: $valor")
        } catch (e: NumberFormatException) {
            Log.e("PDM24", "Erro ao converter para Double: ${e.message}")
        }
    }

    fun clickCalcular(v: View) {
        val valorConta = edtValue.text.toString().toDoubleOrNull() ?: 0.0
        val quantidadePessoas = edtPeople.text.toString().toIntOrNull() ?: 1

        if (quantidadePessoas > 0) {
            val resultado = valorConta / quantidadePessoas
            textValue.setText("Resultado: R$ %.2f".format(resultado))
        }

    }

    fun onChanceCalcular(v: View) {
        val valorConta = edtValue.text.toString().toDoubleOrNull() ?: 0.0
        val quantidadePessoas = edtPeople.text.toString().toIntOrNull() ?: 1

        if (quantidadePessoas > 0 && valorConta > 0) {
            val resultado = valorConta / quantidadePessoas
            textValue.setText("Resultado: R$ %.2f".format(resultado))
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized && ttsSucess) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
            ttsSucess = true
            Log.d("PDM23", "Sucesso na Inicialização")
        } else {
            ttsSucess = false
            Log.e("PDM23", "Falha na Inicialização")
        }
    }
}
