package my.parser

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import my.parser.data.models.Product

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_result)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val elements = intent.getSerializableExtra("products") as ArrayList<Product>

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomRecyclerAdapter(elements, this)

    }

    class CustomRecyclerAdapter(private val values: ArrayList<Product>, private val context: Context) :
            RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

        override fun getItemCount() = values.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_result_elements, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textTitle?.text = values[position].title
            holder.textDescription?.text = values[position].description
            holder.textCost?.text = values[position].cost

            Picasso.get().load(values[position].image_url).into(holder.productImage)

            holder.textTitle?.setOnClickListener {
                val urlRedirect = Uri.parse(values[position].url)

                val data = Intent(Intent.ACTION_VIEW, urlRedirect)
                context.startActivity(data)
            }

        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textTitle: TextView? = null
            var textDescription: TextView? = null
            var textCost: TextView? = null
            var productImage: ImageView? = null

            init {
                textTitle = itemView.findViewById(R.id.text_view_title)
                textDescription = itemView.findViewById(R.id.text_view_description)
                textCost = itemView.findViewById(R.id.text_view_cost)
                productImage = itemView.findViewById(R.id.product_image)
            }
        }

    }


}
