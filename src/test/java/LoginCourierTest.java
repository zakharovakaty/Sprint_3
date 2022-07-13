import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    private CourierMethods courierMethods;
    private int courierId;

    @Before
    public void setUp() {
        courierMethods = new CourierMethods(new RestAssuredClient());
        Courier courier = new Courier();
        courier.setFirstName("testCourierFirstName1");
        courier.setLogin("testCourierName1");
        courier.setPassword("testCourierPassword1");

        courierMethods.createCourier(courier);

        courierId = courierMethods.loginCourier(CourierCredentials.from(courier)).then().extract().body().path("id");
    }

    @After
    public void tearDown() {
        courierMethods.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Check login of courier with correct data")
    public void loginWithCorrectData() {
        CourierCredentials courierCredentials = new CourierCredentials();
        courierCredentials.setLogin("testCourierName1");
        courierCredentials.setPassword("testCourierPassword1");
        ValidatableResponse login = courierMethods.loginCourier(courierCredentials)
                .then().statusCode(200).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Check login of courier with wrong login")
    public void loginWithIncorrectLogin() {
        CourierCredentials courierCredentials = new CourierCredentials();
        courierCredentials.setLogin("wrongLogin0");
        courierCredentials.setPassword("testCourierPassword1");
        ValidatableResponse login = courierMethods.loginCourier(courierCredentials)
                .then().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login of courier with empty password")
    public void loginEmptyPassword() {
        CourierCredentials courierCredentials = new CourierCredentials();
        courierCredentials.setLogin("testCourierName1");
        courierCredentials.setPassword("");
        ValidatableResponse login = courierMethods.loginCourier(courierCredentials)
                .then().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }
}
