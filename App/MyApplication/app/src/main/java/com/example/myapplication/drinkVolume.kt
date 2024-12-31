package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*


class drinkVolume : AppCompatActivity() {
    private lateinit var mqttClient:MQTTClient
    var topic = "thm/volume"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_volume)
        mqttClient = MQTTClient(this, "tcp://broker.mqttdashboard.com",  "ItmeAlaa")
        mqttClient.connect("","",object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                mqttClient.connect("","",object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        mqttClient.publish("thm/drinkMachine",
                            "Vofflume",
                            2,
                            false,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {

                                }

                                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
                                }
                            })
                    }
                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

                    }
                })
                mqttClient.subscribe("thm/volume",
                    2,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg = "Subscribed to: thm/volume"
                            Log.d(this.javaClass.name, msg)
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.d(this.javaClass.name, "Failed to subscribe: thm/volume")
                        }
                    })
                mqttClient.setCallBack(object : MqttCallback {
                    override fun connectionLost(cause: Throwable) {}

                    @Throws(Exception::class)
                    override fun messageArrived(topic: String, message: MqttMessage) {
                        Log.d("tag", "message>>" + String(message.payload))
                        Log.d("tag", "topic>>$topic")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken) {}
                })


            }
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

            }
        })
    }
}