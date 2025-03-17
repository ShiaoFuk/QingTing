package com.example.qingting

class TestClass {
    fun sumOf2(arr: IntArray, target: Int): Pair<Int, Int>? {
        val map = HashMap<Int, Int>()
        for (i in 0 until arr.size) {
            val anotherIndex = map.getOrDefault(target - arr[i], null)
            if (anotherIndex == null) {
                map.put(arr[i], i)
            } else {
                return Pair(i, anotherIndex)
            }
        }
        return null
    }
}

fun main() {

}