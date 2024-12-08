package com.example.constraintlayout

import android.content.Intent
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

        edtValue.addTextChangedListener(this)
        edtPeople.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("PDM24", "Antes de mudar")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24", "Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        val valueText = edtValue.text.toString()
        val peopleText = edtPeople.text.toString()

        if (valueText.isNotBlank() && peopleText.isNotBlank()) {
            try {
                val value = valueText.toDouble()
                val people = peopleText.toDouble()

                val result = value / people

                textValue.text = "R$: %.2f".format(result)
            } catch (e: NumberFormatException) {
                textValue.text = "Por favor, insira números válidos."
            }
        } else {
            textValue.text = ""
        }
    }

    fun clickFalar(v: View){
        val valueText = edtValue.text.toString()
        val peopleText = edtPeople.text.toString()
        val value = valueText.toDouble()
        val people = peopleText.toDouble()

        val result = value / people

        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak("O Resultado é %.2f".format(result), TextToSpeech.QUEUE_FLUSH, null, null)
        }

    }

    override fun onDestroy() {
        // Release TTS engine resources
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("pt", "BR"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("PDM23", "Idioma PT-BR não suportado ou dados ausentes.")
                ttsSucess = false
            } else {
                ttsSucess = true
                Log.d("PDM23", "TTS inicializado com sucesso em PT-BR.")
            }
        } else {
            Log.e("PDM23", "Falha ao inicializar o TTS.")
            ttsSucess = false
        }
    }

    fun compartilharTexto(view: View) {
        val valueText = edtValue.text.toString()
        val peopleText = edtPeople.text.toString()

        if (valueText.toDoubleOrNull() != null && peopleText.toDoubleOrNull() != null) {
            val value = valueText.toDouble()
            val people = peopleText.toDouble()

            if (people > 0) {
                val result = value / people

                val textoParaCompartilhar = """
                💸 *Vamos Rachar!* 💸
                
                💵 Valor total: R$ %.2f
                👥 Pessoas: %.0f
                📊 Valor por pessoa: R$ %.2f
            """.trimIndent().format(value, people, result)

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, textoParaCompartilhar)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            } else {
                textValue.text = "A quantidade de pessoas deve ser maior que zero."
            }
        } else {
            textValue.text = "Por favor, insira números válidos."
        }
    }


}
