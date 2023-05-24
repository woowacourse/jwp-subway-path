package subway.station.presentation;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.station.dto.StationCreateDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationControllerTest {

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  void create() {
    final StationCreateDto stationCreateDto = new StationCreateDto("잠실역");

    RestAssured
        .given()
          .body(stationCreateDto)
          .contentType(ContentType.JSON)
        .when()
          .post("/stations")
        .then()
          .statusCode(HttpStatus.CREATED.value());
  }
}
