package com.example.myapplication

class Drink(val drinkName:String,val volume:String) {
    override fun toString(): String {
        return "drinkName: ${this.drinkName}, volume: ${this.volume}"
    }
}