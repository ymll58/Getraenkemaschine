package com.example.myapplication

class Order(private val drinkNum:String,val size:String ,private val ingredients: List<Drink>) {
    override fun toString(): String {
        return "drinkNum: ${this.drinkNum}, size: ${this.size}, ingredients: ${this.ingredients}"
    }
}