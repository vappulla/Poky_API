package com.example.poke_api

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonImageView: ImageView
    private lateinit var pokemonNameTextView: TextView
    private lateinit var pokemonAgeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val randomPokemonButton = findViewById<Button>(R.id.randomPokemonButton)
        pokemonImageView = findViewById(R.id.pokemonImageView)
        pokemonNameTextView = findViewById(R.id.pokemonNameTextView)
        pokemonAgeTextView = findViewById(R.id.pokemonAgeTextView)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        randomPokemonButton.setOnClickListener {
            getPokeImageURL()
        }
       // getPokeImageURL()
            // getNextImage()
       // Log.d("pokyImageURL", "poky image URL set")
    }



    private fun getPokeImageURL(){
        val client = AsyncHttpClient()
        client["https://pokeapi.co/api/v2/pokemon?limit=1000", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                    //   pokyImageURL = json.jsonObject.getString()
                    val randomIndex = (0 until json.jsonObject.getJSONArray("results").length()).random()
                    val randomPokeURL = json.jsonObject.getJSONArray("results").getJSONObject(randomIndex).getString("url")
                getNextImage(randomPokeURL)


               // Log.d("Pokemon", json.jsonObject.toString())
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon", "Failed to fetch Pokémon")
            }
        }]
    }
    private fun getNextImage(pokemonUrl: String){
        val client = AsyncHttpClient()
        client[pokemonUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                //   pokyImageURL = json.jsonObject.getString()
                val name = json.jsonObject.getString("name")
                val age = json.jsonObject.getInt("id")
                val poky = json.jsonObject.getJSONObject("sprites")
                val imageUrl = poky.getString("front_default")
                pokemonNameTextView.text = "$name"
                pokemonAgeTextView.text = "$age"
                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .into(pokemonImageView)
            }
               // Log.d("Pokemon", "Random Pokémon: $name")


            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("Pokemon", "Failed to fetch Pokémon details")
            }
        }]
    }

}