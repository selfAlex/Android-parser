package my.parser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val elements = intent.getSerializableExtra("elements") as ArrayList<String?>

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomRecyclerAdapter(elements)


    }

    class CustomRecyclerAdapter(private val values: ArrayList<String?>) :
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

}
