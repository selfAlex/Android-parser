package my.parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.content.Intent
import android.widget.Spinner
import android.widget.Switch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun find(view: View) {
        val intent = Intent(this, ResultActivity::class.java)

        val spinnerHardware: Spinner = findViewById(R.id.spinnerHardware)
        val hardware = spinnerHardware.selectedItem.toString();
        intent.putExtra("hardware", hardware)

        val switchShop: Switch = findViewById(R.id.switchShop)
        val useShop = switchShop.isChecked
        intent.putExtra("useShop", useShop)

        val switchTechnodom: Switch = findViewById(R.id.switchTechnodom)
        val useTechnodom = switchTechnodom.isChecked
        intent.putExtra("useTechnodom", useTechnodom)

        val switchSatu : Switch = findViewById(R.id.switchSatu)
        val useSatu = switchSatu.isChecked
        intent.putExtra("useSatu", useSatu)

        startActivity(intent)

    }
}