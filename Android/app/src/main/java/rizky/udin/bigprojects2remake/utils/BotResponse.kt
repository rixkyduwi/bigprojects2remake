package rizky.udin.bigprojects2remake.utils

import android.util.Log
import rizky.udin.bigprojects2remake.utils.Constants.OPEN_GOOGLE
import rizky.udin.bigprojects2remake.utils.Constants.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()
        val context = " "

        return when {
            //
            message.contains("context") -> {
                val context = message

                "silahkan masukan pertanyaan"
            }
            //
            message.contains("?") -> {
                val question = message
                
                question

            }

            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"
                "I flipped a coin and it landed on $result"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }

            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello!"
                    1 -> "Sup"
                    2 -> "Hai!"
                    3 -> "Hello, Ada yang bisa saya bantu?"
                    4 -> "Hai,Senang Bertemeu dengan anda"
                    else -> "error" }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }

            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "maaf saya tidak paham"
                    1 -> "coba pertanyaan lain"
                    2 -> "maaf saya tidak tahu"
                    3 -> "yo ndak tau kok tanya saya"
                    4 -> "tanya yang mau tanya saya"
                    else -> "error"
                }
            }
        }
    }
}