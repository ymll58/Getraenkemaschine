package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

import kotlin.collections.ArrayList

class MainViewModel(application:Application):AndroidViewModel (application){
    private val context: Context = application.applicationContext
    private lateinit var  mqttAndroidClient: MqttAndroidClient

    private var messages:MutableLiveData<ArrayList<Message>> = MutableLiveData(ArrayList())

    fun initClient(serverUri:String,clientID:String) {
        mqttAndroidClient = MqttAndroidClient(context,serverUri,clientID)
    }

    fun connectClient(username:String,pwd:String,callback:(status:Boolean)->Unit) {
        mqttAndroidClient.setCallback(object :MqttCallbackExtended {
            override fun connectionLost(cause: Throwable?) {
                TODO("Not yet implemented")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                addNewMessageToList(message.toString(),topic!!)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                TODO("Not yet implemented")
            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                TODO("Not yet implemented")
            }

        })
        val options = MqttConnectOptions()
        options.password = pwd.toCharArray()
        options.userName = username
        try {
            mqttAndroidClient.connect(options,null,object:IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {

                    callback(true)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    exception?.printStackTrace()
                    callback(false)
                }

            })
        }catch (e:MqttException){
            e.printStackTrace()
            callback(false)
        }
    }

    fun publishMessage(msg:String,qos:Int =1,callback: (status: Boolean) -> Unit) {
        val topic:String = "drink/dr"
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.isRetained = false
            mqttAndroidClient.publish(topic,message,null,object:IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    callback(true)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    callback(false)
                }

            })
        } catch (e:MqttException) {
            e.printStackTrace()
        }
    }

    private  fun addNewMessageToList(msg:String,topic:String)
    {
        val temp = Message(msg,topic)
        val tempData = messages.value!!
        tempData.add(0,temp)
        messages.value = tempData
    }

    fun getLiveMessages(): LiveData<ArrayList<Message>> = messages



}