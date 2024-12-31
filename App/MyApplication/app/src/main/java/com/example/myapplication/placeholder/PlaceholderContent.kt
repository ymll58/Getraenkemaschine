package com.example.myapplication.placeholder

import com.example.myapplication.R
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val normalDrinks: MutableList<PlaceholderItem> = ArrayList()
    val alcoholDrinks:MutableList<PlaceholderItem> = ArrayList()
    val softDrinks: MutableList<PlaceholderItem> = ArrayList()



    init {
        //Juices
        addJuice(createPlaceholderItem(R.drawable.orange,"orange","2.50","200"))
        addJuice(createPlaceholderItem(R.drawable.apple_juice,"apple juice","2.50","200"))
        //alcohol drinks
        addAlcoholDrink(createPlaceholderItem(R.drawable.vodka,"vodka","2.50","30m"))
        //soft drinks
        addSoftDrink(createPlaceholderItem(R.drawable.coca_cola,"Cola","2.50","200"))


    }

    private fun addJuice(item: PlaceholderItem) {
        normalDrinks.add(item)
    }

    private fun addSoftDrink(item: PlaceholderItem) {
        softDrinks.add(item)
    }

    private fun addAlcoholDrink(item: PlaceholderItem) {
        alcoholDrinks.add(item)
    }

    private fun createPlaceholderItem(pic :Int, title: String, price:String,volume:String): PlaceholderItem {
        return PlaceholderItem(pic,title,price,volume)
    }



    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val pic :Int, val title: String,val price:String,val volume:String) {

    }
}