package my.parser

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import my.parser.data.models.Product
import kotlin.collections.ArrayList

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_result)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        @Suppress("UNCHECKED_CAST")
        val elements = intent.getSerializableExtra("products") as ArrayList<Product>

        val adapter = CustomRecyclerAdapter(elements, elements,this)
        recyclerView.adapter = adapter

        val costSwitcher: FloatingActionButton = findViewById(R.id.cost_switcher)

        val searchInput: EditText = findViewById(R.id.search_input)
        searchInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {adapter.filter.filter(p0)}
            override fun afterTextChanged(p0: Editable?) {}  })

    }


}
