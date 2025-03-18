package com.ladyprogram.superhero.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ladyprogram.superhero.R
import com.ladyprogram.superhero.data.Superhero
import com.ladyprogram.superhero.data.SuperheroService
import com.ladyprogram.superhero.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    lateinit var superhero: Superhero
    lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra("SUPERHERO_ID")!!
        getSuperheroById(id)

        binding.navigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_biography -> {
                    binding.appearanceContent.root.visibility = View.GONE
                    binding.statsContent.root.visibility = View.GONE
                    binding.biographyContent.root.visibility = View.VISIBLE

                }
                R.id.action_appearence -> {
                    binding.appearanceContent.root.visibility = View.VISIBLE
                    binding.statsContent.root.visibility = View.GONE
                    binding.biographyContent.root.visibility = View.GONE
                }
                R.id.action_stats -> {
                    binding.appearanceContent.root.visibility = View.GONE
                    binding.statsContent.root.visibility = View.VISIBLE
                    binding.biographyContent.root.visibility = View.GONE

                }
            }

            true
        }

        binding.navigationBar.selectedItemId = R.id.action_biography
    }



    fun loadData() {
        //Toast.makeText(this, superhero.name, Toast.LENGTH_LONG).show()
        Picasso.get().load(superhero.image.url).into(binding.pictureImageView)

        supportActionBar?.title = superhero.name
        supportActionBar?.subtitle = superhero.biography.realName


        //BIOGRAPHY
        binding.biographyContent.publisherTextView.text = superhero.biography.publisher
        binding.biographyContent.placeOfBirthTextView.text = superhero.biography.placeOfBirth
        binding.biographyContent.alignmentTextView.text = superhero.biography.alignment

        binding.biographyContent.occupationTextView.text = superhero.work.occupation
        binding.biographyContent.baseTextView.text = superhero.work.base

        //APPEARANCE
        binding.appearanceContent.raceTextView.text = superhero.appearance.race
        binding.appearanceContent.genderTextView.text = superhero.appearance.gender
        binding.appearanceContent.eyeColorTextView.text = superhero.appearance.eyeColor
        binding.appearanceContent.hairColorTextView.text = superhero.appearance.hairColor
        binding.appearanceContent.weightTextView.text = superhero.appearance.getWeightKg()
        binding.appearanceContent.heightTextView.text = superhero.appearance.getHeightCm()

        //STATS
        binding.statsContent.intelligenceTextView.text = superhero.stats.intelligence
        binding.statsContent.strengthTextView.text = superhero.stats.strength
        binding.statsContent.speedTextView.text = superhero.stats.speed
        binding.statsContent.durabilityTextView.text = superhero.stats.durability
        binding.statsContent.powerTextView.text = superhero.stats.power
        binding.statsContent.combatTextView.text = superhero.stats.combat

    }


    fun getRetrofit(): SuperheroService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://superheroapi.com/api/dff2a9fd483ae9f087a0715eacf4ef53/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SuperheroService::class.java)
    }


    fun getSuperheroById(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = getRetrofit()
                superhero = service.findSuperheroById(id)

                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}