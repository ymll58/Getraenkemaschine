package com.example.myapplication


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentItemBinding
import com.example.myapplication.placeholder.PlaceholderContent.PlaceholderItem
import kotlinx.coroutines.delay
import org.eclipse.paho.client.mqttv3.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class DrinkViewAdapter(

    val values: List<PlaceholderItem>

) : RecyclerView.Adapter<DrinkViewAdapter. ViewHolder>() {
    lateinit var context: Context
    lateinit var jsonCreator: JsonCreator
    lateinit var orderList:OrderList
    val drinkList = ArrayList<Drink>()
    val orders = ArrayList<Order>()



    class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = itemView.findViewById(R.id.cardImage)
        val contentView: TextView = itemView.findViewById(R.id.card_title)
        val volume: TextView = itemView.findViewById(R.id.volume)
        val price: TextView= itemView.findViewById(R.id.price)
        val button: Button = itemView.findViewById(R.id.order)


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        context = holder.itemView.context
        val item = values[position]
        holder.imageView.setImageResource(item.pic)
        holder.contentView.text = item.title
        holder.price.text = item.price
        holder.volume.text = item.volume

        var mqttClient =MQTTClient(context, "tcp://broker.mqttdashboard.com",  "ItmeAlaa")


        holder.button.setOnClickListener {
            orders.clear()
            drinkList.clear()
            mqttClient.connect("","",object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    drinkList.add(Drink(values[position].title,values[position].volume))
                    orders.add(Order(drinkList.size.toString(),"A",drinkList))
                    orderList = OrderList(orders)
                    jsonCreator = JsonCreator(orderList)
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

                            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                                Log.d(this.javaClass.name, "Failed to publish message to topic")
                            }
                        })
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")
                    Toast.makeText(context, "MQTT Connection fails: ${exception.toString()}", Toast.LENGTH_SHORT).show()

                }

            })
        }
    }






    override fun getItemCount(): Int = values.size



}