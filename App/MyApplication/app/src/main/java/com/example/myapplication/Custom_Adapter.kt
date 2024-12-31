package com.example.myapplication

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.myapplication.LoadingScreen
import com.example.myapplication.R

//import jdk.vm.ci.code.site.Site


/**
 *
 *  listView.onItemClickListener =
OnItemClickListener { parent, view, position, id ->
val o: Any = listView.getItemAtPosition(position)

Toast.makeText(baseContext, o.toString(), Toast.LENGTH_SHORT).show()
}
 */




class Custom_Adapter(private val context: Activity, private val drinkname: ArrayList<String>, private val volumes: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.list_activity, volumes) {



    companion object{
        var del_cond:Boolean=false ;
        var pos:Int=-1
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_activity, null, true)

        val drink = rowView.findViewById(R.id.drinkitem) as TextView
        //  val imageView = rowView.findViewById(R.id.icon) as ImageView
        val volume = rowView.findViewById(R.id.itemvolume) as TextView

        // val Btn=rowView.findViewById<FloatingActionButton>(R.id.delete_Btn)

        val delete=rowView.findViewById(R.id.delete_Btn) as ImageView



        var loading= LoadingScreen()




        drink.text = drinkname[position]
        //imageView.setImageResource(imgid[position])
        volume.text = volumes[position]



        delete.setOnClickListener(View.OnClickListener {
            var parsed:Int=(loading.companion.volumelist!![position]).toInt()
            loading.companion.glass_total-=parsed

            loading.companion.removeItem(position)
            loading.companion.glass_full=false
            loading.companion.tot_txt?.setText("Total :"+"${loading.companion.glass_total}"+"/"+"${loading.companion.glassint}")
        })

        /**drink.setOnClickListener(){
        // notifyDataSetChanged()
        //Log.i("this is the pos :",position.toString())

        del_cond=true
        pos=position
        }
         **/

        return rowView
    }


    /** override fun getItem(position: Int): jdk.vm.ci.code.site.Site? {
    return drinkname[position]
    }**/
    val myButtonClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            val parentRow = v.parent as View
            val listView = parentRow.parent as ListView
            val position = listView.getPositionForView(parentRow)
            Log.i("this is the position ",position.toString())

        }
    }




}