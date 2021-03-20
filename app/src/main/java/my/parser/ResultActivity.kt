package my.parser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import java.util.concurrent.TimeUnit


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

//        val my_data = parsing()
//        val elements = listOf<String?>()
//
//        if (my_data != null) {
//            for (d in my_data) {
//                elements.toMutableList().add(d["title"])
//            }
//        }

        val elements = intent.getSerializableExtra("elements") as ArrayList<String?>

        println("ELEMENTS IS: $elements")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomRecyclerAdapter(elements)


    }

    fun parsing(): List<Map<String, String?>>? {

        var shop: List<Map<String, String?>>? = null

        val hardware = intent.getSerializableExtra("hardware").toString()

        val useShop = intent.getSerializableExtra("useShop")
        val useTechnodom = intent.getSerializableExtra("useTechnodom")
        val useSatu = intent.getSerializableExtra("useSatu")

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


            }
        })

        return shop

    }

}

class CustomRecyclerAdapter(private val values: ArrayList<String?>):
        RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

        override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_result_elements, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text?.text = values[position]
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView? = null

        init {
            text = itemView.findViewById(R.id.text_view_1)
        }
    }

}
