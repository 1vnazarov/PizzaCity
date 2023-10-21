class PizzaCity(
    private val city: String, prices: List<Double>,
    private val addons: List<String>
) : Drink, CheckPhoto {
    private val names = listOf("Неополитанская", "Римская", "Сицилийская", "Тирольская") // Ассортимент одинаковый
    private val catalog = mutableListOf<Pizza>()
    init {
        for ((index, value) in names.withIndex()) {
            catalog.add(Pizza(value, prices[index]))
        }
    }
    private var totalCustomers = 0
    fun sale(index: Int) {
        println("Спасибо за покупку пиццы \"${names[index]}\" в городе $city")
        catalog[index].countSale++
        totalCustomers++
    }
    // Так как у меня для всех городов один класс, существует поле addons, в котором хранятся доступные для города услуги
    private var discount = 0.0
    private var agreeShowCheck = 0
    private var disagreeShowCheck = 0
    override fun showCheckPhoto() {
        if (!(addons.contains("showCheck"))) return
        if (getInput("""
        У вас есть фотография чека?
        1 - Да
        2 - Нет

        """.trimIndent()) == "1") {
            agreeShowCheck++
            discount += 50
            println("Активирована скидка 50 рублей")
        }
        else disagreeShowCheck++
    }

    private val coffee = Coffee(200.0)
    private var disagreeBuyCoffee = 0 // Согласившихся можно смотреть по продажам кофе
    override fun saleDrink() {
        if (!(addons.contains("drink"))) return
        if (getInput("""
        Вы будете кофе?
        1 - Да
        2 - Нет

        """.trimIndent()) == "1") {
            coffee.countSale++
            println("С Вас ${coffee.price} рублей")
        }
        else disagreeBuyCoffee++
    }

    private fun showPercent(a: Double, text: String) {
        println("${a / totalCustomers * 100}% $text")
    }
    fun showStat() {
        var money = 0.0
        for (i in names.indices) {
            money += catalog[i].countSale * catalog[i].price
            println("Продано пиццы \"${names[i]}\": ${catalog[i].countSale}")
        }
        if (addons.contains("drink")) {
            val coffeeSum = coffee.countSale * coffee.price
            money += coffeeSum
            println("Продано ${coffee.name.lowercase()}: ${coffee.countSale} (выручка за кофе: $coffeeSum)")
        }
        money -= discount
        println("Всего заработано денег: $money")

        if (addons.contains("showCheck")) {
            println("Скидка в $discount вычтена")
            showPercent(agreeShowCheck.toDouble(), "показывают фотографию чека")
            showPercent(disagreeShowCheck.toDouble(), "не показывают фотографию чека")
        }
        if (addons.contains("drink")) {
            showPercent(coffee.countSale.toDouble(), "купили кофе")
            showPercent(disagreeBuyCoffee.toDouble(), "отказались покупать кофе")
        }
    }
}