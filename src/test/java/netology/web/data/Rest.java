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

    public static void sendPurchaseRequestOnCredit(CartInfo cartInfo, String way, int status) {

        given()
                .spec(requestSpec)
                .body(gson.toJson(cartInfo))
                .when()
                .post(way)
                .then().log().all()
                .statusCode(status);
    }
}
