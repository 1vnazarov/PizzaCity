fun main() {
    val pizzas = listOf(PizzaCity("Санкт-Петербург", listOf(500.0, 400.0, 300.0, 200.0, 250.0), listOf("drink")),
        PizzaCity("Москва", listOf(900.0, 800.0, 700.0, 600.0), listOf("showCheck")),
        PizzaCity("Красноярск", listOf(590.0, 500.0, 450.0, 400.0, 200.0, 50.0, 70.0), listOf("drink", "showCheck", "sauce")))
    var pizza: PizzaCity?
    while (true) {
        pizza = pizzas.getOrNull(getInput("Добрый день!\n" + prettyStringList(pizzas.mapIndexed{i, v -> "${i + 1} - ${v.getCity()}\n"}.toString())
            + "Иначе - Выход из программы\nВыберите город: ").toInt() - 1)
        if (pizza == null) break
        selectPizza(pizza)
    }
}

fun selectPizza(pizza: PizzaCity) {
    var index: Int
    do {
        index = getInput(prettyStringList(pizza.getNames().mapIndexed{i, v -> "${i + 1} - $v пицца\n"}.toString())
                + "0 - Вывести статистику\nВыберите пиццу: ").toInt() - 1
        when (index) {
            in 0..3 -> pizza.sale(index)
            -1 -> pizza.showStat()
            else -> {
                println("Некорректный выбор пиццы")
                continue
            }
        }
    } while (index !in -1..3)
    if (index != -1) {
        pizza.showCheckPhoto()
        pizza.saleDrink()
        pizza.saleSauce()
    }
}
fun prettyStringList(list: String) = list.replace("[", "").replace("]", "").replace(", ", "")
fun getInput(prompt: String): String {
    print(prompt)
    return readln()
}
fun safeNaN(a: Double) = if (a.isNaN()) 0.0 else a