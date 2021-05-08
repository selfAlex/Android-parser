package my.parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import com.google.gson.Gson

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_main)
    }

    fun find(view: View) {
        val intent = Intent(this, ResultActivity::class.java)

        val spinnerHardware: Spinner = findViewById(R.id.spinnerHardware)
        val hardware = spinnerHardware.selectedItem.toString()

        val switchShop: Switch = findViewById(R.id.switchShop)
        val useShop = switchShop.isChecked

        val switchTechnodom: Switch = findViewById(R.id.switchTechnodom)
        val useTechnodom = switchTechnodom.isChecked

        val switchTomas : Switch = findViewById(R.id.switchTomas)
        val useTomas = switchTomas.isChecked

        val buttonFind : Button = findViewById(R.id.button)
        buttonFind.text = getString(R.string.parsingText)
        buttonFind.isEnabled = false

        val jsonBody = JSONObject()

        jsonBody.put("hardware", hardware)
        jsonBody.put("use_shop", useShop)
        jsonBody.put("use_forcecom", useTechnodom)
        jsonBody.put("use_tomas", useTomas)

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(6000, TimeUnit.SECONDS)
                .writeTimeout(6000, TimeUnit.SECONDS)
                .readTimeout(6000, TimeUnit.SECONDS)
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

                val dataShop = jsonHandler.shop
                val dataForcecom = jsonHandler.forcecom
                val dataTomas = jsonHandler.tomas

                val productsShop = ArrayList<Product>()

                if (dataShop != null) {
                    for (product in dataShop) {
                        productsShop.add(Product(product["image_url"], product["title"], product["description"], product["cost"], product["url"]))
                    }
                }

                val storeShop = Store("Shop", productsShop)

                val productsForcecom = ArrayList<Product>()

                if (dataForcecom != null) {
                    for (product in dataForcecom) {
                        productsForcecom.add(Product(product["image_url"], product["title"], product["description"], product["cost"], product["url"]))
                    }
                }

                val storeForcecom = Store("Shop", productsForcecom)

                val productsTomas = ArrayList<Product>()

                if (dataTomas != null) {
                    for (product in dataTomas) {
                        productsTomas.add(Product(product["image_url"], product["title"], product["description"], product["cost"], product["url"]))
                    }
                }

                val storeTomas = Store("Shop", productsTomas)

                val elements = ArrayList<Product>()
                elements.addAll(storeShop.products)
                elements.addAll(storeForcecom.products)
                elements.addAll(storeTomas.products)


                intent.putExtra("elements", elements)
                println("ИНТЕНТ СОЗДАН")
                startActivity(intent)
            }
        })

    }


    }


class JsonHandler(shop: List<Map<String, String?>>, forcecom: List<Map<String, String?>>, tomas: List<Map<String, String?>>) {
    var shop: List<Map<String, String?>>? = shop
    var forcecom: List<Map<String, String?>>? = forcecom
    var tomas: List<Map<String, String?>>? = tomas

    override fun toString(): String {
        return "Shop: ${this.shop}, 'Forcecom: ${this.forcecom}, Tomas: ${this.tomas}"
    }

}

data class Product(val image_url: String?, val title: String?, val description: String?, val cost: String?, val url: String?) : Serializable
data class Store(val name: String, val products: ArrayList<Product>) : Serializable

