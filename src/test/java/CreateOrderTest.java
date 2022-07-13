import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderMethods orderMethods;
    private int track;
    private Order order;

    public CreateOrderTest(List<String> color) {
        this.order = new Order("testFirstName1", "testLastName1", "testAddress, 123",
                "1", "+7 800 111 11 11", 1, "2022-06-30", "testComment1", color);
    }

    @Before
    public void setUp() {
        orderMethods = new OrderMethods(new RestAssuredClient());
    }

    @After
    public void tearDown() {
        orderMethods.cancelOrder(track);
    }

    @Parameterized.Parameters(name = "Тестовые данные: Color={0}")
    public static Object createOrderData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK, GREY")},
                {List.of("")}
        };
    }

    @Test
    @DisplayName("Check creation of order with different colors")
    public void createOrder() {
        int track = orderMethods.createOrder(order)
                .then().statusCode(201).and().extract().body().path("track");
    }
}
