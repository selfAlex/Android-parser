package my.parser

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.net.URL

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        print("RECYCLER VIEW НАЙДЕН")

        val elements = intent.getSerializableExtra("elements") as ArrayList<Product>
        println("ИНТЕНТ ПОЛУЧЕН")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomRecyclerAdapter(elements)


    }

    class CustomRecyclerAdapter(private val values: ArrayList<Product>) :
            RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

        override fun getItemCount() = values.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_result_elements, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textTitle?.text = values[position].title
            holder.textDescription?.text = values[position].description
            Picasso.get().load(values[position].image_url).into(holder.productImage)
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textTitle: TextView? = null
            var textDescription: TextView? = null
            var productImage: ImageView? = null

            init {
                textTitle = itemView.findViewById(R.id.text_view_title)
                textDescription = itemView.findViewById(R.id.text_view_description)
                productImage = itemView.findViewById(R.id.product_image)
            }
        }

    }

}
