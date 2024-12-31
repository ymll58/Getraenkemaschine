package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Drink_Types : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.glass_types)

        val smallglass = findViewById<ImageView>(R.id.smallimage)
        val mediumglass=findViewById<ImageView>(R.id.medium_img)
        val largeglass=findViewById<ImageView>(R.id.large_img)

        smallglass.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,LoadingScreen::class.java)
                intent.putExtra("volume","100")
                startActivity(intent)
                }
        )
        mediumglass.setOnClickListener(
            View.OnClickListener {

                val intent = Intent(this,LoadingScreen::class.java)
                intent.putExtra("volume","150")
                startActivity(intent) }
        )
        largeglass.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,LoadingScreen::class.java)
                intent.putExtra("volume","200")
                startActivity(intent) }
        )

    }
}