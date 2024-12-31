package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LoadingScreen
import com.example.myapplication.R
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.*


class Favorite_Page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_favorite_page)
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
      val jsonstring:String=jsonload()


        val JsonObject = JSONObject(jsonstring)

        val favoritelist=findViewById<ListView>(R.id.favoritelist)

        Log.i("test",  JsonObject.get("id").toString() )

        Log.i("the object ", JsonObject.toString())

        Log.i("the jsonstring :",jsonstring)

        var ids: JSONObject = JsonObject.getJSONObject("ingredients")

        val x: Iterator<*> = JsonObject.keys()
        var jsonArray = JSONArray()

        while (x.hasNext()) {
            val key = x.next() as String
            jsonArray.put(JsonObject[key])
        }
        val length = jsonArray.length()
        val listContents: MutableList<String> = ArrayList(length)
        for (i in 0 until length) {
            listContents.add(jsonArray.getString(i))
        }


       val favoriteadapter=ArrayAdapter(this,android.R.layout.simple_list_item_1, listContents )

      favoritelist.adapter=favoriteadapter
    }



    fun jsonload():String {
        var fis: FileInputStream? = null
        val sb = StringBuilder()
        try {
            fis = openFileInput(LoadingScreen.Companion.FILE_NAME)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)

            var text: String?
            while (br.readLine().also { text = it } != null) {
                sb.append(text).append("\n")
            }
            Log.i("the file loaded :",sb.toString())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    return sb.toString()
    }

}