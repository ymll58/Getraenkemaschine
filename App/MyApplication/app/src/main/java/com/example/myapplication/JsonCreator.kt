package com.example.myapplication


import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class JsonCreator(val orderList:OrderList) {
    val gson = Gson()
    var jsonTutsList = gson.toJson(orderList)

    fun getJson():String {
        jsonTutsList.replace("[","")

        Log.i("the jdon after :",jsonTutsList.toString())

        return jsonTutsList
    }
}