package my.parser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val hardware = intent.getSerializableExtra("hardware").toString()

        val useShop = intent.getSerializableExtra("useShop")
        val useTechnodom = intent.getSerializableExtra("useTechnodom")
        val useSatu = intent.getSerializableExtra("useSatu")

    }

}
