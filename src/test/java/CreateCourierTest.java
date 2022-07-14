import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private static CourierMethods courierMethods;
    private int courierId;

    @BeforeClass
    public static void setUp() {
        courierMethods = new CourierMethods(new RestAssuredClient());
    }

    @After
    public void tearDown() {
        if (courierId != 0)
        courierMethods.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Check creation of courier with valid data")
    public void createNewCourier() {

        Courier courier = new Courier();
        courier.setFirstName("testCourierFirstName1");
        courier.setLogin("testCourierName1");
        courier.setPassword("testCourierPassword1");

        boolean isCourierCreated = courierMethods.createCourier(courier)
                .then().statusCode(201)
                .extract()
                .path("ok");

        courierId = courierMethods.loginCourier(CourierCredentials.from(courier)).then().extract().body().path("id");
    }

    @Test
    @DisplayName("Check creation of courier without password should return error")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier();
        courier.setFirstName("testCourierFirstName1");
        courier.setLogin("testCourierName1");
        courier.setPassword("");

        ValidatableResponse response = courierMethods.createCourier(courier)
                .then().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check creation of courier without login should return error")
    public void createCourierWithoutUserName() {
        Courier courier = new Courier();
        courier.setFirstName("testCourierFirstName1");
        courier.setLogin("");
        courier.setPassword("testCourierPassword1");

        ValidatableResponse response = courierMethods.createCourier(courier)
                .then().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check creation of duplicate courier should return error")
    public void createDuplicateCourier(){
        Courier courier = new Courier();
        courier.setFirstName("testCourierFirstName1");
        courier.setLogin("testCourierName1");
        courier.setPassword("testCourierPassword1");

        boolean isCourierCreated = courierMethods.createCourier(courier)
                .then().statusCode(201)
                .extract()
                .path("ok");

        courierId = courierMethods.loginCourier(CourierCredentials.from(courier)).then().extract().body().path("id");

        ValidatableResponse createDuplicate = courierMethods.createCourier(courier)
                .then().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }
}

