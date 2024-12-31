package com.example.myapplication


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.view.Window
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity

import com.example.myapplication.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.json.JSONArray
import java.io.*


//import com.example.bestellung.Custom_Adapter.
//note don t forget to add the chose glass weight by the json creation so that we can detetct the used cup weight is correct



class LoadingScreen : AppCompatActivity() {




    /**
    open fun removeItem(i: Int) {
    makeToast("Removed: " + itemlist.get(i))
    itemlist.removeAt(i)
    listView.setAdapter(adapter)
    }
     **/
    lateinit var mqttClient: MQTTClient
    lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        glass_total=0
        glass_full=false
        context = this
        itemlist.clear()
        volumelist.clear()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        //the favorite btn
        var favorite=findViewById<ExtendedFloatingActionButton>(R.id.favorite_Btn)

        // access the spinners
        val spinner = findViewById<Spinner>(R.id.spinner)

        //list of drinks available in the machine
        val drinks = resources.getStringArray(R.array.Drinks)

        var builder =JsonBuilder()

        //  val del=findViewById<ImageView>(R.id.delete_Btn)
        var drink_state:Boolean=false
        var volume_state:Boolean=false

        //temporary  volume chosen
        var volume_chosen:String = "empty"

        //temporary  drink variable , in case the user changes his mind while using the spinner
        var drink_chosen:String="empty"

        //spinner adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drinks)


        //the volume bar
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        //the add button
        val add_button=findViewById<ImageView>(R.id.AddButton)

        //the select drink text id
        val txtview=findViewById<TextView>(R.id.txtView)

        //the seekbar progress tracker
        val seek_progress=findViewById<TextView>(R.id.progress)

        //recieve the glass type that the user picked passed from the glass_types activity
        val glass_type=intent.getStringExtra("volume")
        glassint=Integer.parseInt(glass_type)

        var type = ""
        if(glass_type == "100") {
            type = "C"
        }
        if (glass_type == "150") {
            type = "B"
        }
        if(glass_type == "200") {
            type = "A"
        }


        listView= findViewById<View>(R.id.listView) as ListView

        myListAdapter=Custom_Adapter(this,itemlist,volumelist)



        // add to favorites function
        favorite.setOnClickListener(){
            var obj = MutableList(itemlist.size) { cocktail("empty", "empty") }
            obj.clear()
            val gson = Gson()

            for (i in 0..(itemlist.size - 1)) {
                obj.add(cocktail(itemlist[i], volumelist[i]))
            }
            val jsonstring=gson.toJson(obj)



            Log.i("fav json", json_tostring)
            val builder = AlertDialog.Builder(this, androidx.appcompat.R.style.AlertDialog_AppCompat_Light)
            builder.setTitle("add to favorites")
            val inflater = layoutInflater.inflate(R.layout.popup,null)
            builder.setView(inflater)
            var  drinkid:String=""
            var editText  = inflater.findViewById<EditText>(R.id.editText)




            builder.setPositiveButton("OK") { dialogInterface, i ->
                val favoritebuilder = JsonBuilder(

                    "id" to  editText.text.toString(),
                    "Order" to "",
                    "drinkNumbers" to itemlist.size.toString(),
                    "drinktype" to glass_type.toString(),
                    "ingredients" to JsonBuilder(
                        "drink" to arrayOf(jsonstring),

                        )
                )



                json_tostring = (favoritebuilder.toString()).replace("\\", "")

                //we need the \ when saving the drinks to favorite
                savejson(favoritebuilder.toString()+",")

                Toast.makeText(applicationContext, "EditText is " + editText.text.toString(), Toast.LENGTH_SHORT).show()
            }





            builder.show()

        }






        seekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                //read the seekbar discrete variables(from 0 to 4) and convert them liquor-volumes
                var vol:Int =0

                Log.i("this is the volume :",glass_type.toString())

                //this bit will track and display the seekbar value selected depending on its X axis
                val value = progress * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max


                if(progress==0){
                    vol=0
                    seek_progress.setText(""+vol)

                }

                if(progress==1) {
                    vol=glassint/4
                    seek_progress.setText("" + vol)

                }
                if(progress==2) {
                    vol=glassint/3
                    seek_progress.setText("" + vol )

                }
                if(progress==3) {
                    vol=glassint/2
                    seek_progress.setText("" + vol)

                }

                //  seek_progress.setText("" + progress)
                seek_progress.setX(seekBar.x + value + seekBar.thumbOffset / 2)


                //textView.setY(100); just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar
                //the volume that the user chose

                if(progress>0) {
                    volume_chosen = vol.toString()
                    volume_state=true
                }
                if((glass_total+vol)>glassint){
                    volume_state=false
                    seekBar.progress=0
                    volume_chosen="0"
                    spinner.adapter=adapter
                    Toast.makeText(this@LoadingScreen,"the volume chosen is larger than the remaining empty glass speace :",Toast.LENGTH_SHORT).show()


                }



            }




            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is started.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is stopped.
                /**   Toast.makeText(
                this@LoadingScreen,
                "Current value is " + seekBar.progress,
                Toast.LENGTH_SHORT
                ).show()**/
            }
        })







        //list of volumes the user can select
        val volumes=resources.getStringArray(R.array.Volume)



        //list of the volumes







        val myButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                val parentRow = v.parent as View
                val listView = parentRow.parent as ListView
                val position = listView.getPositionForView(parentRow)
                Toast.makeText(this@LoadingScreen,"the position :$position",Toast.LENGTH_SHORT)
            }
        }





        /**
        fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item: String = listView.getItemAtPosition(position) as String
        Toast.makeText(this, "You selected : $item", Toast.LENGTH_SHORT).show()
        }
         **/


        1
        if (spinner != null  ) {



            //custom adapter for the list

            //listView.adapter=myListAdapter

            // val itemsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, itemlist)

            spinner.adapter = adapter
            //volume_spinner.adapter=volume_adapter

            //if the item has been clicked then delete it




            val position: SparseBooleanArray = listView!!.checkedItemPositions
            listView!!.setOnItemClickListener { adapterView, view, i, l ->


                Log.i("the items :::",itemlist.toString())
                Log.i("the volume :::",volumelist.toString())

                myListAdapter!!.notifyDataSetChanged()

                //itemlist.removeAt(i)
                // volumelist.removeAt(i)
                //   position.clear()

                //itemlist.removeAt(i)


                //itemsAdapter.remove(itemlist.get(i))


                //  itemsAdapter.notifyDataSetChanged()
                //itemlist.remove(drinks[i].toString())
                //android.widget.Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), android.widget.Toast.LENGTH_SHORT).show()

            }

            //////

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {


                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {


                    /**
                    if (parent.getItemAtPosition(position).equals("Select Drink:")) {
                    //do nthng
                    }**/

                    if(parent.getId() == R.id.spinner) {
                        if (parent.getItemAtPosition(position).equals("Select Drink:")){

                            //do nthng
                        }
                        else {


                            drink_chosen=drinks[position].toString()

                            txtview.setText(drinks[position].toString())

                            adapter.notifyDataSetChanged()

                            drink_state=true

                            //listView.adapter=adapter_item
                            /**Toast.makeText(
                            this@LoadingScreen,
                            getString(R.string.selected_item) + " " +
                            "" + drinks[position], Toast.LENGTH_SHORT
                            ).show()**/
                        }

                    }

                }



                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action



                }


            }


        }


        add_button.setOnClickListener(){


            if( (drink_state==true) && (volume_state==true) ) {

                jsonload()




                if(glass_total>=glassint){
                    glass_full=true
                    volume_chosen="0"
                }
                var glass_int:Int=volume_chosen.toInt()

                /**    if(glass_full==true && ( (glass_int-glass_total)>0) ){
                var parsedInt =  ( volumelist[(volumelist.size)-1]).toInt()
                parsedInt+=(glass_int-glass_total)
                glass_total=glass_int- glass_total
                volumelist[volumelist.size-1]=parsedInt.toString()
                tot_txt?.setText("Total :"+"$glass_total"+"/"+"$glassint")
                listView!!.adapter= myListAdapter
                }**/


                companion.tot_txt=findViewById<TextView>(R.id.total_txt)

                if(glass_full==false){
                    glass_total+=glass_int
                    itemlist.add(drink_chosen)
                    volumelist.add(volume_chosen)
                    tot_txt?.setText("Total :"+"$glass_total"+"/"+"$glassint")
                    seekBar.progress=0
                    listView!!.adapter = myListAdapter
                    myListAdapter!!.notifyDataSetChanged()






                    Log.i("the drink ",builder.toString())



                    Log.i(volumelist.toString(), itemlist.toString()) }
                else{
                    Toast.makeText(this@LoadingScreen," the glass is full , do you want to place your order :",Toast.LENGTH_SHORT).show()
                    seekBar.progress=0
                }
                spinner.adapter=adapter




            }

            if( (glassint- glass_total)< (glassint/4) ) {
                Log.i("entered successfully","bez")
                if(volumelist.size>0){
                    var parsedInt =  ( volumelist[(volumelist.size)-1]).toInt()
                    parsedInt+=(glassint-glass_total)
                    glass_total+=(glassint- glass_total)
                    volumelist[volumelist.size-1]=parsedInt.toString()
                    tot_txt?.setText("Total :"+"$glass_total"+"/"+"$glassint")
                    listView!!.adapter= myListAdapter
                    Log.i("total", glass_total.toString())
                    Log.i("rest ",parsedInt.toString())
                    glass_full=true
                }
            }


            else if((drink_state==true) && (volume_state==false)) {
                txtview.setText("Select Drink:")
                Log.i("the items in error  :",itemlist.toString())
                Log.i("the volume in error :", volumelist.toString())
                Toast.makeText(
                    this@LoadingScreen,
                    "Please choose a volume " +
                            "" , Toast.LENGTH_SHORT
                ).show()
            }

            else if((drink_state==false) && (volume_state==true)) {
                seekBar.progress=0
                Log.i("the items in error  :",itemlist.toString())
                Log.i("the volume in error :", volumelist.toString())
                Toast.makeText(
                    this@LoadingScreen,
                    "Please choose a drink " +
                            "" , Toast.LENGTH_SHORT
                ).show()
            }

            else{
                Log.i("the items in error  :",itemlist.toString())
                Log.i("the volume in error :", volumelist.toString())
                Toast.makeText(
                    this@LoadingScreen,
                    "Please choose a drink  and a suitable volume" +
                            "" , Toast.LENGTH_SHORT
                ).show()
            }

            txtview.setText("Select Drink:")
            drink_state=false
            volume_state=false
        }




        /**
        if(Companion.del_cond==true){
        Log.i("the pos :",Companion.pos.toString())
        itemlist.removeAt(Companion.pos)
        volumelist.removeAt(Companion.pos)
        myListAdapter.notifyDataSetChanged()
        Companion.del_cond=false
        Companion.pos=-1
        }
         **/


        fun createFile() : File {
            val fileName = "myJson"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            if (storageDir != null) {
                if (!storageDir.exists()) {
                    storageDir.mkdir()
                }
            }
            return File.createTempFile(
                fileName,
                ".json",
                storageDir
            )
        }


        //write the Json file
        val order=findViewById<ExtendedFloatingActionButton>(R.id.Order_Btn)

        order.setOnClickListener(){
            if(glass_full) {
                var obj = MutableList(itemlist.size) { Drink("empty", "empty") }
                obj.clear()
                val gson = Gson()

                for (i in 0..(itemlist.size - 1)) {
                    obj.add(Drink(itemlist[i], volumelist[i]))
                }


                val jsonString = gson.toJson(obj)
                Log.i("the json b4 :", jsonString)

                val jsonFormattedString: String = jsonString.replace("\\", "+")
                Log.i("the json after :", jsonFormattedString)


                var orderList = ArrayList<Order>()
                orderList.add(Order(itemlist.size.toString(), type, obj))
                var order = OrderList(orderList)
                var jsonCreator = JsonCreator(order)
                var json_tostring: String = (builder.toString()).replace("\\", "")

                Log.i("json to string ", jsonCreator.getJson())

                mqttClient = MQTTClient(this, "tcp://broker.mqttdashboard.com", "tektek")
                mqttClient.connect("", "", object : IMqttActionListener {

                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        mqttClient.publish("thm/drinkMachine",
                            jsonCreator.getJson(),
                            2,
                            false,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    val msg = "success publish"
                                    Log.d(this.javaClass.name, msg)
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                                }

                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
                                }
                            })
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")
                        Toast.makeText(
                            context,
                            "MQTT Connection fails: ${exception.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                })
                savejson(json_tostring)
                itemlist.clear()
                volumelist.clear()
                glass_total = 0
                glass_full = false
                tot_txt?.setText("Total :" + "${0}" + "/" + "${glassint}")
                listView!!.adapter = myListAdapter
                Log.i("builder side :", builder.toString())

            }
        }






    }


    companion object { //list of the drinks the user selected
        const val FILE_NAME = "Favorite.json"
        var json_tostring: String = ""
        var glassint:Int=0
        var tot_txt:TextView ?=null
        //total of the liquid poured in the glass
        var glass_total:Int=0
        var glass_full:Boolean=false

        var itemlist = arrayListOf<String>()
        var listView: ListView? = null

        //list of volume on the listview
        var volumelist = arrayListOf<String>()
        var myListAdapter: Custom_Adapter? = null

        var context: Context? = null

        // function to remove an item given its index in the grocery list.
        fun removeItem(i: Int) {

            Log.i("item removed ", "here")


            itemlist!!.removeAt(i)
            volumelist!!.removeAt(i)
            listView!!.adapter = myListAdapter
        }
    }
    var companion = Companion

    fun savejson(txt:String) {
        val text =txt
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND)
            fos.write(text.toByteArray())

            Toast.makeText(
                this, "Saved to " + filesDir + "/" + FILE_NAME,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun jsonload() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(FILE_NAME)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
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
    }


}




