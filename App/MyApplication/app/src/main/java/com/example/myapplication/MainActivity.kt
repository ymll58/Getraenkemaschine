package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.eclipse.paho.client.mqttv3.*
import java.nio.channels.InterruptedByTimeoutException

class MainActivity : AppCompatActivity() {
    lateinit var mqttClient: MQTTClient
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mqttClient = MQTTClient(this, "tcp://broker.mqttdashboard.com",  "ItmeAlaa")
        setContentView(R.layout.activity_main0)
        getSupportActionBar()?.hide();
        val clickme = findViewById<FloatingActionButton>(R.id.simple);
        val mix = findViewById<FloatingActionButton>(R.id.mix)
        val fav = findViewById<FloatingActionButton>(R.id.FavoriteBtn)

        clickme.setOnClickListener() {
            val intent = Intent(this,simpleDrink::class.java)
            startActivity(intent)
        }
        mix.setOnClickListener(){
                startActivity( Intent(this,Drink_Types::class.java))
        }
        fav.setOnClickListener(){
            startActivity( Intent(this,Favorite_Page::class.java))
        }


    }


}



