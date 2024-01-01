package model

data class Rectangle(
    var x: Int,
    var y: Int,
    var width: Int,
    var length: Int
) : Comparable<Rectangle> {
    fun getArea(): Long {
        return width * length.toLong();
    }

    override fun compareTo(other: Rectangle): Int {
        return when {
            getArea() < other.getArea() -> -1
            getArea() > other.getArea() -> 1
            else -> when {
                x.toLong() * y < other.x.toLong() * other.y -> -1
                x.toLong() * y > other.x.toLong() * other.y -> 1
                else -> 0
            }
        }
    }
}