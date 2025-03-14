package com.ladyprogram.superhero.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ladyprogram.superhero.R
import com.ladyprogram.superhero.adapters.SuperheroAdapter
import com.ladyprogram.superhero.data.Superhero
import com.ladyprogram.superhero.data.SuperheroService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SuperheroAdapter

    var superheroList: List<Superhero> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        adapter = SuperheroAdapter(superheroList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        getRetroFit()

    }

    fun getRetroFit () {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://superheroapi.com/api/dff2a9fd483ae9f087a0715eacf4ef53/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SuperheroService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val result = service.findSuperheroesByName("super")

            superheroList = result.results

            CoroutineScope(Dispatchers.Main).launch {
                adapter.items = superheroList
                adapter.notifyDataSetChanged()

            }

            //for (superhero in result.results) {
                //Log.i("API", "${superhero.id} -> ${superhero.name}")
            }
        }
    }
