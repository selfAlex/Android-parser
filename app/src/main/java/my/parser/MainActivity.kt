package my.parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.switchmaterial.SwitchMaterial
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

        val buttonFind : Button = findViewById(R.id.button)
        buttonFind.setOnClickListener{find()}
    }

    private fun find() {
        val intent = Intent(this, ResultActivity::class.java)

        val jsonBody = getJsonBody()

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
                val dataRaw = response.body?.string()
                val gson = Gson()
                val data = gson.fromJson(dataRaw, JsonHandler::class.java)

                val productsShop = toProductList(data.shop)
                val productsForcecom = toProductList(data.forcecom)
                val productsTomas = toProductList(data.tomas)

                val products = ArrayList<Product>()
                products.addAll(productsShop)
                products.addAll(productsForcecom)
                products.addAll(productsTomas)

                intent.putExtra("products", products)
                startActivity(intent)
            }

        })

    }

    private fun getJsonBody(): JSONObject {
        val jsonBody = JSONObject()

        val spinnerHardware: Spinner = findViewById(R.id.spinnerHardware)
        val switchShop: SwitchMaterial = findViewById(R.id.switchShop)
        val switchTechnodom: SwitchMaterial = findViewById(R.id.switchTechnodom)
        val switchTomas : SwitchMaterial = findViewById(R.id.switchTomas)

        jsonBody.put("hardware", spinnerHardware.selectedItem.toString())
        jsonBody.put("use_shop", switchShop.isChecked)
        jsonBody.put("use_forcecom", switchTechnodom.isChecked)
        jsonBody.put("use_tomas", switchTomas.isChecked)

        return jsonBody
    }

    private fun toProductList(elements: List<Map<String, String?>>?): ArrayList<Product> {
        val productList = ArrayList<Product>()

        if (elements != null) {
            for (element in elements) {
                productList.add(Product(element["image_url"], element["title"], element["description"], element["cost"], element["url"]))
            }
        }

        return productList
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
