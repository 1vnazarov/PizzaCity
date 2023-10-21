class PizzaCity(
    private val city: String, prices: List<Double>,
    private val addons: List<String>
) : Drink, CheckPhoto, Sauce {
    private val names = listOf("Неополитанская", "Римская", "Сицилийская", "Тирольская") // Ассортимент одинаковый
    private val catalog = mutableListOf<Pizza>()
    init {
        for ((index, value) in names.withIndex()) {
            catalog.add(Pizza(value, prices[index]))
        }
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
            coffee.saleCountForEachPizza[pizzaIndex] = coffee.saleCountForEachPizza.getOrDefault(pizzaIndex, 0) + 1
        }
        else disagreeBuyCoffee++
    }
    private val cheeseSauce = PizzaSauce("Сырный соус", 70.0)
    private val bbqSauce = PizzaSauce("Соус барбекю", 50.0)
    override fun saleSauce() {
        if (!(addons.contains("sauce"))) return
        when (getInput("""
        Выберите соус к пицце:
        1 - Сырный соус
        2 - Соус барбекю
        Иначе - Без соуса
        
        """.trimIndent())) {
            "1" -> {
                cheeseSauce.countSale++
                println("С Вас ${cheeseSauce.price}")
            }
            "2" -> {
                bbqSauce.countSale++
                println("С Вас ${bbqSauce.price}")
            }
        }
    }
    private fun showPercent(a: Double, text: String) {
        println("${a / totalCustomers * 100}% $text")
    }
    fun showStat() {
        var money = 0.0
        for (i in names.indices) {
            money += catalog[i].countSale * catalog[i].price
            print("Продано пиццы \"${names[i]}\": ${catalog[i].countSale} ")
            if (addons.contains("drink")) {
                println("(кофе к ней: ${coffee.saleCountForEachPizza.getOrDefault(i, 0)} / ${coffee.countSale} шт., ${coffee.saleCountForEachPizza.getOrDefault(i, 0).toDouble() / coffee.countSale * 100}%)")
            }
            else println()
        }
        if (addons.contains("drink")) {
            money += coffee.partStat()
        }

        if (addons.contains("sauce")) {
            money += cheeseSauce.partStat()
            money += bbqSauce.partStat()
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