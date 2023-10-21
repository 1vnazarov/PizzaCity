abstract class Product {
    abstract val name: String
    abstract val price: Double
    var countSale = 0

    fun partStat(): Double {
        val cost = countSale * price
        println("Продано ${name.lowercase()}: $countSale (выручка за ${name.lowercase()}: $cost)")
        return cost
    }
}