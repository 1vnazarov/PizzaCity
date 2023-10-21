class Coffee (override val price: Double) : Product() {
    override val name = "Кофе"
    val saleCountForEachPizza = mutableMapOf<Int, Int>()
}