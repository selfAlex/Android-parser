package my.parser

import android.content.Intent
import my.parser.main.MainViewModel

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.flow.collect

import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        val intent = Intent(this, ResultActivity::class.java)

        val button: Button = findViewById(R.id.button)

        val spinnerHardware: Spinner = findViewById(R.id.spinnerHardware)
        val switchShop: SwitchMaterial = findViewById(R.id.switchShop)
        val switchTechnodom: SwitchMaterial = findViewById(R.id.switchTechnodom)
        val switchTomas : SwitchMaterial = findViewById(R.id.switchTomas)

        fun showMessage(text: String) {
            return Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            viewModel.loadData(
                    spinnerHardware.selectedItem.toString(),
                    switchShop.isChecked,
                    switchTechnodom.isChecked,
                    switchTomas.isChecked
            )
        }

        lifecycleScope.launchWhenStarted {

            viewModel.state.collect { event ->
                when (event) {

                    is MainViewModel.ParserEvent.Success -> {
                        intent.putExtra("products", event.resultText)

                        button.text = getString(R.string.find)
                        button.isEnabled = true

                        startActivity(intent)
                    }

                    is MainViewModel.ParserEvent.Failure -> {

                        showMessage("Failure occurred")

                        button.text = getString(R.string.find)
                        button.isEnabled = true
                    }

                    is MainViewModel.ParserEvent.Loading -> {
                        button.text = getString(R.string.loadingText)
                        button.isEnabled = false
                    }

                    else -> Unit
                }
            }

        }

    }
}