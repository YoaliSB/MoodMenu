package soup.com.soup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null
    private val locSpanish = Locale("spa", "MEX")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false;
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(locSpanish)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = editText!!.text.toString()
        val calificacion = getMood(text)
        var sopa : String = ""
        var bebida : String = ""
        var plato : String = ""
        if (calificacion >= 0) {
            if (calificacion == 0) {
                sopa = "crema de elota"
                bebida = "agua de jamaica"
                plato = "milanesa"
            } else {
                sopa = "sopa de verduras"
                bebida = "soda italiana"
                plato = "pavo en escabeche"
            }
        }
        else{
            sopa = "caldito de pollo"
            bebida = "chocolate"
            plato = "salmón a la plancha"
        }

        tts!!.speak("tomate " + bebida + " y come " + sopa + " y " + plato,
                TextToSpeech.QUEUE_FLUSH, null,"")
    }

    private fun getMood(text: String): Int{
        var sum = 0;
        val positiveWords : HashMap<String, Int> = hashMapOf(
            "aceptar" to 1,
            "gracias" to 2,
            "compasión" to 3,
            "tolerancia" to 4,
            "feliz" to 5,
            "bien" to 6,
            "agradecido" to 7,
            "compartir" to 8,
            "bonito" to 9,
            "posible" to 10,
            "útil" to 11,
            "oportunidad" to 12,
            "esperanza" to 13,
            "celebrar" to 14,
            "sonreir" to 10,
            "amor" to 15)
        val negativeWords :  HashMap<String, Int> = hashMapOf(
            "feo" to 9,
            "horrible" to 15,
            "mal" to 10,
            "triste" to 5,
            "enojado" to 5,
            "llorar" to 4,
            "estrés" to 6,
            "nervios" to 2,
            "gritar" to 4,
            "terrible" to 13,
            "depresión" to 15
        )
        val array = text.split(' ')
        array.forEach{
            if(positiveWords.containsKey(it)){
                sum += positiveWords.get(it)!!
            } else{
                if(negativeWords.containsKey(it)){
                    sum -= negativeWords.get(it)!!
                }
            }
        }
        return sum;
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

}