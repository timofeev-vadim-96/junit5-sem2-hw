package seminars.second.simple_shopping_cart;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShopTest {

    public static List<Product> getStoreItems() {
        List<Product> products = new ArrayList<>();

        String[] productNames = {"bacon", "beef", "ham", "salmon", "carrot", "potato", "onion", "apple", "melon", "rice", "eggs", "yogurt"};
        Double[] productPrice = {170.00d, 250.00d, 200.00d, 150.00d, 15.00d, 30.00d, 20.00d, 59.00d, 88.00d, 100.00d, 80.00d, 55.00d};
        Integer[] stock = {10, 10, 10, 10, 10, 10, 10, 70, 13, 30, 40, 60};

        for (int i = 0; i < productNames.length; i++) {
            products.add(new Product(i + 1, productNames[i], productPrice[i], stock[i]));
        }

        return products;
    }

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    private Shop shop;
    private Cart cart;
    private final Random random = new Random();

    @BeforeEach
    void setup() {
        shop = new Shop(getStoreItems());
        cart = new Cart(shop);
    }


/*todo визуальное отображение списка товаров в тесте
            ID | Название  | Цена, р. | Кол-во в магазине, шт.
            1  | bacon     | 170.0    | 10
            2  | beef      | 250.0    | 10
            3  | ham       | 200.0    | 10
            4  | salmon    | 150.0    | 10
            5  | carrot    | 15.0     | 10
            6  | potato    | 30.0     | 10
            7  | onion     | 20.0     | 10
            8  | apple     | 59.0     | 70
            9  | melon     | 88.0     | 13
            10 | rice      | 100.0    | 30
            11 | eggs      | 80.0     | 40
            12 | yogurt    | 55.0     | 60
*/

    /**
     * 2.1. Разработайте модульный тест для проверки, что общая стоимость
     * корзины с разными товарами корректно рассчитывается
     * <br><b>Ожидаемый результат:</b>
     * Стоимость корзины посчиталась корректно
     */
    @Test
    void priceCartIsCorrectCalculated() {
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(2);

        assertEquals(420, cart.getTotalPrice());
    }

    /**
     * 2.2. Создайте модульный тест для проверки, что общая стоимость
     * корзины с множественными экземплярами одного и того же продукта корректно рассчитывается.
     * <br><b>Ожидаемый результат:</b>
     * Стоимость корзины посчиталась корректно
     */
    @Test
    void priceCartProductsSameTypeIsCorrectCalculated() {
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(2);
        cart.addProductToCartByID(2);

        assertEquals(670, cart.getTotalPrice());
    }

    /**
     * 2.3. Напишите модульный тест для проверки, что при удалении
     * товара из корзины происходит перерасчет общей стоимости корзины.
     * <br><b>Ожидаемый результат:</b>
     * Вызывается метод пересчета стоимости корзины, стоимость корзины меняется
     */
    @Test
    void whenChangingCartCostRecalculationIsCalled() {
        cart.addProductToCartByID(7);
        cart.addProductToCartByID(4);
        cart.addProductToCartByID(8);
        cart.removeProductByID(4);

        assertEquals(79.0, cart.getTotalPrice());
    }

    /**
     * 2.4. Разработайте модульный тест для проверки, что при добавлении определенного количества товара в корзину,
     * общее количество этого товара в магазине соответствующим образом уменьшается.
     * <br><b>Ожидаемый результат:</b>
     * Количество товара в магазине уменьшается на число продуктов в корзине пользователя
     */
    @Test
    void quantityProductsStoreChanging() {
        List<Product> products = shop.getProductsShop();
        cart.addProductToCartByID(10);

        assertEquals(29, products.get(9).getQuantity());
    }

    /**
     * 2.5. Создайте модульный тест для проверки, что если пользователь забирает все имеющиеся продукты о
     * пределенного типа из магазина, эти продукты больше не доступны для заказа.
     * <br><b>Ожидаемый результат:</b>
     * Больше такой продукт заказать нельзя, он не появляется на полке
     */
    @Test
    void lastProductsDisappearFromStore() {
        int quantityZeroElem = shop.getProductsShop().get(0).getQuantity();
        for (int i = 0; i < quantityZeroElem; i++) {
            cart.addProductToCartByID(1);
        }
        int before = cart.cartItems.size();
        System.setOut(new PrintStream(output));
        cart.addProductToCartByID(1);
        int after = cart.cartItems.size();

        assertEquals(before, after);
        assertEquals("Этого товара нет в наличии", output.toString().trim());
    }

    /**
     * 2.6. Напишите модульный тест для проверки, что при удалении товара из корзины,
     * общее количество этого товара в магазине соответствующим образом увеличивается.
     * <br><b>Ожидаемый результат:</b>
     * Количество продуктов этого типа на складе увеличивается на число удаленных из корзины продуктов
     */
    @Test
    void deletedProductIsReturnedToShop() {
        int beforeQuantity = shop.getProductsShop().get(4).getQuantity();
        cart.addProductToCartByID(5);
        cart.removeProductByID(5);
        int afterQuantity = shop.getProductsShop().get(4).getQuantity();

        assertEquals(afterQuantity, beforeQuantity);
    }

    /**
     * 2.7. Разработайте параметризованный модульный тест для проверки,
     * что при вводе неверного идентификатора товара генерируется исключение RuntimeException.
     * <br><b>Ожидаемый результат:</b>
     * Исключение типа RuntimeException и сообщение Не найден продукт с id
     * *Сделать тест параметризованным
     */
    @ParameterizedTest
    @ValueSource(ints = {194})
    //здесь указывается набор из параметров
    void incorrectProductSelectionCausesException(int i) {
        assertThatThrownBy(() -> cart.addProductToCartByID(i)).isInstanceOf(RuntimeException.class);
    }

    /**
     * 2.8. Создайте модульный тест для проверки, что при попытке удалить из корзины больше товаров,
     * чем там есть, генерируется исключение RuntimeException.удаляет продукты до того, как их добавить)
     * <br><b>Ожидаемый результат:</b> Исключение типа NoSuchFieldError и сообщение "В корзине не найден продукт с id"
     */
    @Test
    void incorrectProductRemoveCausesException() {
        assertThatThrownBy(() -> cart.removeProductByID(1)).isInstanceOf(RuntimeException.class);
    }

    /**
     * 2.9. Нужно восстановить тест
     */
    @Test
    void testSum() {
        cart.addProductToCartByID(2); // 250
        cart.addProductToCartByID(2); // 250

        assertThat(500.0).isEqualTo(cart.getTotalPrice());
    }

    /**
     * 2.10. Нужно оптимизировать тестовый метод, согласно следующим условиям:
     * <br> 1. Отображаемое имя - "Advanced test for calculating TotalPrice"
     * <br> 2. Тест повторяется 10 раз
     * <br> 3. Установлен таймаут на выполнение теста 70 Миллисекунд (unit = TimeUnit.MILLISECONDS)
     * <br> 4. После проверки работоспособности теста, его нужно выключить
     */

    @Disabled
    @DisplayName("Advanced test for calculating TotalPrice")
    @RepeatedTest(10)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void calculateTotalPrice(){
        cart.addProductToCartByID(2);
        assertThat(250.0).isEqualTo(cart.getTotalPrice());
    }
}