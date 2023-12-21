package model

data class Rectangle(
    var x: Int,
    var y: Int,
    var width: Int,
    var length: Int
) {
    fun getArea(): Int {
        return width * length;
    }
}