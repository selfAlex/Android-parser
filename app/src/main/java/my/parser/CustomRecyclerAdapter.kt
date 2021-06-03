package my.parser

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import my.parser.data.models.Product
import java.util.*
import kotlin.collections.ArrayList

class CustomRecyclerAdapter(
        private val values: ArrayList<Product>,
        private var valuesFiltered: ArrayList<Product>,
        private val context: Context) :

        RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>(),
        Filterable {

    override fun getItemCount() = valuesFiltered.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_result_elements, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.textTitle?.text = valuesFiltered[position].title
        holder.textTitle?.setOnClickListener {
            val urlRedirect = Uri.parse(valuesFiltered[position].url)

            val data = Intent(Intent.ACTION_VIEW, urlRedirect)
            context.startActivity(data)
        }

        holder.textDescription?.text = valuesFiltered[position].description
        holder.textCost?.text = valuesFiltered[position].cost.toString()

        Picasso.get().load(valuesFiltered[position].image_url).into(holder.productImage)

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

    fun reverse() {
        valuesFiltered.reverse()
        notifyDataSetChanged()
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
                            newValues.add(value)
                        }
                    }

                    newValues
                }

                val filterResults = FilterResults()
                filterResults.values = valuesFiltered

                return filterResults

            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                valuesFiltered = p1?.values as ArrayList<Product>
                notifyDataSetChanged()
            }

        }
    }

}