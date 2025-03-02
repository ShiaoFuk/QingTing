package com.example.qingting

class TestClass {

}

fun main() {
    val a = readln().toInt()
    var b: Int? = null
    if (a == 99) {
        b = a
    }
    if (b is Int) {
        println(b)
    }
}