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
                "cambio" to 4,
                "tolerancia" to 5,
                "agradable" to 3,
                "bien" to 3,
                "agradecido" to 9,
                "compartir" to 10,
                "genial" to 11,
                "sonreir" to 12,
                "bonito" to 13,
                "posible" to 14,
                "confianza" to 15,
                "bendecido" to 20,
                "prosperidad" to 14,
                "útil" to 12,
                "oportunidad" to 16,
                "esperanza" to 18,
                "maravilloso" to 21,
                "exitoso" to 22,
                "celebrar" to 23,
                "sonreir" to 17,
                "amor" to 25
        )
        val negativeWords :  HashMap<String, Int> = hashMapOf(

                "feo" to 9,
                "horrible" to 15,
                "mal" to 10,
                "triste" to 5,
                "enojado" to 5,
                "llorar" to 4,
                "frustrado" to 6,
                "estrés" to 6,
                "nervios" to 2,
                "gritar" to 4,
                "terrible" to 13,
                "depresión" to 25,
                "angustia" to 7,
                "temor" to 8,
                "desesperación" to 20,
                "melancolía" to 9,
                "incertidumbre" to 10,
                "pánico" to 12,
                "ira" to 18,
                "inseguro" to 15,
                "cansancio" to 9,
                "enfermo" to 16,
                "miedo" to 11 ,
                "dolor" to 13,
                "solo" to 18

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