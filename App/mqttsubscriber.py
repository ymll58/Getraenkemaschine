# python3.6

from dis import findlinestarts
from email import message
from lib2to3.pgen2 import driver
import random
#include json library
import json
import csv
import time
from tkinter.font import names
from paho.mqtt import client as mqtt_client
import serial
import time


broker = 'broker.mqttdashboard.com'
port = 1883
topic = "thm/drinkMachine"
# generate client ID with pub prefix randomly
client_id = f'python-mqtt-{random.randint(0, 100)}'
serialcomm = serial.Serial('COM6', 9600)

# username = 'emqx'
# password = 'public'
 #data = json.loads(message)
       # drinks = data["orders"]
        #for d in drinks:
         #   print(d["drinkNum"])
lines = []
#erialcomm = serial.Serial('COM6', 9600)

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client


def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        finishedOrder = "Order"
        data = json.loads(msg.payload.decode())
        orders = data["orders"]
        for order in orders:
                finishedOrder += ","+order["drinkNum"]+","+order["size"]
                for drink in order['ingredients']:
                    finishedOrder += ","+drink.get("drinkName")+","+drink.get("volume")

        serialcomm.write(finishedOrder.encode())

    client.subscribe(topic)
    client.on_message = on_message


def run():
    client = connect_mqtt()
    subscribe(client)
    client.loop_forever()




if name == 'main':
    run()