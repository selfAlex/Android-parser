package my.parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.content.Intent
import android.widget.Spinner
import android.widget.Switch
import com.google.gson.Gson

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun find(view: View) {
        val intent = Intent(this, ResultActivity::class.java)

        val spinnerHardware: Spinner = findViewById(R.id.spinnerHardware)
        val hardware = spinnerHardware.selectedItem.toString()
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

        val elements = ArrayList<String?>()

        val jsonBody = JSONObject()

        jsonBody.put("hardware", "Motherboards")
        jsonBody.put("use_shop", 1)
        jsonBody.put("use_forcecom", 0)
        jsonBody.put("use_satu", 0)

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build()

        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
                .method("POST", requestBody)
                .url("http://192.168.1.22:5000")
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(call)
                println(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body?.string()

                val gson = Gson()

                println("Объект GSON Создан")

                val jsonHandler = gson.fromJson(data, JsonHandler::class.java)

                println("JSON Успешно обработан")

                val shopData = jsonHandler.shop

                if (shopData != null) {
                    for (product in shopData) {
                        elements.add(product["title"])
                    }
                }

                intent.putExtra("elements", elements)
                startActivity(intent)
            }
        })

    }


    }


class JsonHandler(shop: List<Map<String, String?>>, forcecom: List<Map<String, String?>>, satu: List<Map<String, String?>>) {
    var shop: List<Map<String, String?>>? = null
    var forcecom: List<Map<String, String?>>? = null
    var satu: List<Map<String, String?>>? = null

    override fun toString(): String {
        return "Shop: ${this.shop}, 'Forcecom: ${this.forcecom}, Satu: ${this.satu}"
    }

}

