package my.parser

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

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val elements = intent.getSerializableExtra("elements") as ArrayList<Product>

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
//            holder.textDescription?.text = values[position].description
            holder.textCost?.text = "Cost: " + values[position].cost

            Picasso.get().load(values[position].image_url).into(holder.productImage)

            holder.textRedirect?.text = values[position].url

        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textTitle: TextView? = null
//            var textDescription: TextView? = null
            var textCost: TextView? = null
            var productImage: ImageView? = null
            var textRedirect: TextView? = null

            init {
                textTitle = itemView.findViewById(R.id.text_view_title)
//                textDescription = itemView.findViewById(R.id.text_view_description)
                textCost = itemView.findViewById(R.id.text_view_cost)
                productImage = itemView.findViewById(R.id.product_image)
                textRedirect = itemView.findViewById(R.id.text_view_redirect)
            }
        }

    }

}
