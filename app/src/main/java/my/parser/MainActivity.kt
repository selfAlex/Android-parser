package my.parser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.content.Intent
import android.widget.Spinner
import android.widget.Switch
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val elements = ArrayList<String?>()

        var result = ArrayList<String?>()

        var shop: List<Map<String, String?>>? = null


        val hardware_ = intent.getSerializableExtra("hardware").toString()

        val useShop_ = intent.getSerializableExtra("useShop")
        val useTechnodom_ = intent.getSerializableExtra("useTechnodom")
        val useSatu_ = intent.getSerializableExtra("useSatu")

        val url = "http://192.168.1.22:5000/"

        val jsonBody = JSONObject()

        jsonBody.put("hardware", "Graphic cards")
        jsonBody.put("use_shop", 1)
        jsonBody.put("use_forcecom", 0)
        jsonBody.put("use_satu", 0)

        var data: String? = ""

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
                println("FAIL!!!!!")
                println("CALL")
                println(call)
                println("e")
                println(e)
            }

            override fun onResponse(call: Call, response: Response) {
                println("NICEEEEEEE")
                data = response.body?.string()

                println("AFTER ALL")

                val gson = Gson()

                val example = gson.fromJson(data, Example1::class.java)

                shop = example.shop

                val my_data = shop

                if (my_data != null) {
                    for (d in my_data) {
                        elements.add(d["title"])
                    }
                }

                intent.putExtra("elements", elements)
                startActivity(intent)
            }
        })

    }


//        while( true ) {
//            println("ELEMENTS IS: $elements")
//        }

//        while ( true ) {
//            if (intent.extras?.containsKey("elements")!!) {
//                println("element intent isn't empty !" )
//                break
//            } else {
//                    println("element intent is empty !")
//                    continue }
//        }



    }


class Example1 {
    var shop: List<Map<String, String?>>? = null
    var forcecom: List<Map<String, String?>>? = null
    var satu: List<Map<String, String?>>? = null

    constructor(): super()

    constructor(shop: List<Map<String, String?>>, forcecom: List<Map<String, String?>>, satu: List<Map<String, String?>>) : super() {
        this.shop = shop
        this.forcecom = forcecom
        this.satu = satu
    }

    override fun toString(): String {
        return "[SHOP.KZ: ${this.shop}, FORCECOM.KZ: ${this.forcecom}, SATU.KZ: ${this.satu}]"
    }

}

