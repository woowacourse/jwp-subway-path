package subway.utils;

import io.restassured.RestAssured;

public class BaseTest {

    public static final String LINE_URL = "/lines";

    protected void setUp(int port) {
        RestAssured.port = port;
    }
}
