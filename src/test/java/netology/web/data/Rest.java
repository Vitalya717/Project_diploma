package netology.web.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;

public class Rest {
    static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Gson gson = new Gson();

    private Rest() {
    }

    public static void sendPurchaseRequestWithActiveCard(CartInfo cartInfo) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post("/api/v1/pay")
                .then().log().all()
                .statusCode(200);
    }

    public static void sendCreditRequestWithActiveCard(CartInfo cartInfo) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post("/api/v1/credit")
                .then().log().all()
                .statusCode(200);
    }

    public static void sendPurchaseRequestWithBlockedCard(CartInfo cartInfo) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post("/api/v1/pay")
                .then().log().all()
                .statusCode(403);
    }
    public static void sendCreditRequestWithBlockedCard(CartInfo cartInfo) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post("/api/v1/credit")
                .then().log().all()
                .statusCode(403);
    }
    public static void sendPurchaseRequestWithActiveBankCardAndInvalidData(CartInfo cartInfo) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post("/api/v1/pay")
                .then().log().all()
                .statusCode(400);
    }
    public static void sendPurchaseRequestCreditWithActiveBankCardAndInvalidData(CartInfo cartInfo) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post("/api/v1/credit")
                .then().log().all()
                .statusCode(400);
    }

}
