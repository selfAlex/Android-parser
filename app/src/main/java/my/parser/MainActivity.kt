package my.parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
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

                val productsShop = getProductsShop(data)
                val productsForcecom = getProductsForcecom(data)
                val productsTomas = getProductsTomas(data)

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

    private fun getProductsShop(data: JsonHandler): ArrayList<Product> {
        val dataShop = data.shop

        val productsShop = ArrayList<Product>()

        if (dataShop != null) {
            for (product in dataShop) {
                productsShop.add(Product(product["image_url"], product["title"], product["description"], product["cost"], product["url"]))
            }
        }

        return productsShop

    }

    private fun getProductsForcecom(data: JsonHandler): ArrayList<Product> {
        val dataForcecom = data.forcecom

        val productsForcecom = ArrayList<Product>()

        if (dataForcecom != null) {
            for (product in dataForcecom) {
                productsForcecom.add(Product(product["image_url"], product["title"], product["description"], product["cost"], product["url"]))
            }
        }

        return productsForcecom
    }

    private fun getProductsTomas(data: JsonHandler): ArrayList<Product> {
        val dataTomas = data.tomas

        val productsTomas = ArrayList<Product>()

        if (dataTomas != null) {
            for (product in dataTomas) {
                productsTomas.add(Product(product["image_url"], product["title"], product["description"], product["cost"], product["url"]))
            }
        }

        return productsTomas
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
