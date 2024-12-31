package com.example.myapplication

class OrderList(private val orders: List<Order>) {
    override fun toString(): String {
        return "orders : ${this.orders}"
    }
}