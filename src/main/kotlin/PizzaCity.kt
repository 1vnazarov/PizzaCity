class PizzaCity(
    private val city: String, prices: List<Double>,
    private val addons: List<String>
) : Drink, CheckPhoto, Sauce {
    fun getCity() = city
    private val names = listOf("Неополитанская", "Римская", "Сицилийская", "Тирольская") // Ассортимент одинаковый
    fun getNames() = names
    private val catalog = mutableListOf<Pizza>()
    private val addonPrices = prices.subList(names.size, prices.size)
    init {
        for ((index, value) in names.withIndex())
            catalog += Pizza(value, prices[index])
    }
    private var totalCustomers = 0
    private var pizzaIndex = -1
    fun sale(index: Int) {
        println("Спасибо за покупку пиццы \"${names[index]}\" за ${catalog[index].price} в городе $city")
        pizzaIndex = index
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
    private var coffee: Coffee? = null
    init {
        if (addonPrices.isNotEmpty()) coffee = Coffee(addonPrices[0]) // Предполагается, если есть кофе, его цена в листе первая
    }
    private fun getCoffe() = coffee!!
    private var disagreeBuyCoffee = 0 // Согласившихся можно смотреть по продажам кофе
    override fun saleDrink() {
        if (!(addons.contains("drink"))) return
        if (getInput("""
        Вы будете кофе?
        1 - Да
        2 - Нет

        """.trimIndent()) == "1") {
            getCoffe().countSale++
            println("С Вас ${getCoffe().price} рублей")
            getCoffe().saleCountForEachPizza[pizzaIndex] = getCoffe().saleCountForEachPizza.getOrDefault(pizzaIndex, 0) + 1
        }
        else disagreeBuyCoffee++
    }
    private val sauceNames = listOf("Сырный соус", "Соус барбекю")
    private val sauces = mutableListOf<PizzaSauce>()
    init {
        if (addons.contains("sauce"))
            for ((index, value) in sauceNames.withIndex())
                sauces += PizzaSauce(value, addonPrices[index + if (addons.contains("drink")) 1 else 0])
    }
    override fun saleSauce() {
        if (!(addons.contains("sauce"))) return
        var index = getInput( "Выберите соус:\n" + prettyStringList(sauceNames.mapIndexed{i, v -> "${i + 1} - $v\n"}.toString())
                + "Иначе - Без соуса\n").toIntOrNull()
        if (index !is Int) return
        index--
        when (index) {
            in 0..1 -> {
                sauces[index].countSale++
                println("С Вас ${sauces[index].price} рублей")
            }
        }
    }
    private fun showPercent(a: Double, text: String) = println("${safeNaN(a / totalCustomers * 100)}% $text")
    fun showStat() {
        var money = 0.0
        for (i in names.indices) {
            money += catalog[i].countSale * catalog[i].price
            print("Продано пиццы \"${names[i]}\": ${catalog[i].countSale} ")
            if (addons.contains("drink"))
                println("(кофе к ней: ${getCoffe().saleCountForEachPizza.getOrDefault(i, 0)} / ${getCoffe().countSale} шт., ${safeNaN(getCoffe().saleCountForEachPizza.getOrDefault(i, 0).toDouble() / getCoffe().countSale * 100)}%)")
            else println()
        }
        if (addons.contains("drink")) money += getCoffe().partStat()
        if (addons.contains("sauce")) sauces.forEach{money += it.partStat()}

        money -= discount
        println("Всего заработано денег: $money")

        if (addons.contains("showCheck")) {
            println("Скидка в $discount вычтена")
            showPercent(agreeShowCheck.toDouble(), "показывают фотографию чека")
            showPercent(disagreeShowCheck.toDouble(), "не показывают фотографию чека")
        }
        if (addons.contains("drink")) {
            showPercent(getCoffe().countSale.toDouble(), "купили кофе")
            showPercent(disagreeBuyCoffee.toDouble(), "отказались покупать кофе")
        }
    }
}