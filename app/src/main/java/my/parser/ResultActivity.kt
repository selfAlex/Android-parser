package my.parser

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import my.parser.data.models.Product
import java.util.*
import kotlin.collections.ArrayList

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_result)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val costSwitcher: FloatingActionButton = findViewById(R.id.cost_switcher)
        val searchInput: EditText = findViewById(R.id.search_input)

        val elements = intent.getSerializableExtra("products") as ArrayList<Product>

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CustomRecyclerAdapter(elements, elements,this)
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    class CustomRecyclerAdapter(private val values: ArrayList<Product>, private var valuesFiltered: ArrayList<Product>, private val context: Context) :
            RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>(), Filterable {

        override fun getItemCount() = valuesFiltered.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_result_elements, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textTitle?.text = valuesFiltered[position].title
            holder.textDescription?.text = valuesFiltered[position].description
            holder.textCost?.text = valuesFiltered[position].cost

            Picasso.get().load(valuesFiltered[position].image_url).into(holder.productImage)

            holder.textTitle?.setOnClickListener {
                val urlRedirect = Uri.parse(valuesFiltered[position].url)

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

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(p0: CharSequence?): FilterResults {
                    val key: String = p0.toString()

                    valuesFiltered = if (key.isEmpty()) {
                        values
                    } else {
                        val newValues = ArrayList<Product>()

                        for (value in values) {
                            if (value.title.toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT))) {
                                newValues.add(value);
                            }
                        }

                        newValues
                    }

                    val filterResults = FilterResults()
                    filterResults.values = valuesFiltered
                    return filterResults

                }

                override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                    valuesFiltered = p1?.values as ArrayList<Product>
                    notifyDataSetChanged()
                }

            }
        }

    }


}
