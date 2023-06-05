package subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:initializeTestDb.sql")
class SubwayApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("특정 빈 노선에 역 추가하기")
    @Test
    void createSectionEmptyLine() throws JsonProcessingException {
        RestAssured.given()
                   .log()
                   .all()
                   .body(objectMapper.writeValueAsString(new SectionRequest(7L, 8L, 6)))
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .post("/sections/3")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/lines/3");
    }

    @DisplayName("특정 비지 않은 노선에 역 추가하기")
    @Test
    void createSectionInLine() throws JsonProcessingException {
        RestAssured.given()
                   .log()
                   .all()
                   .body(objectMapper.writeValueAsString(new SectionRequest(1L, 3L, 5)))
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .post("/sections/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/lines/1");
    }

    @DisplayName("특정 노선에서 두 역 사이에 있는 역 제거하기")
    @Test
    void deleteSectionBetweenStationInLine() {
        RestAssured.given()
                   .log()
                   .all()
                   .body(1L)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .delete("sections/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.NO_CONTENT.value())
                   .extract();

    }



    @DisplayName("특정 노선에서 끝에 있는 역 제거하기")
    @Test
    void deleteSectionInLine() {
        RestAssured.given()
                   .log()
                   .all()
                   .body(2L)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .delete("sections/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.NO_CONTENT.value())
                   .extract();

    }
}
