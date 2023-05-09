package subway.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.InitialStationsAddRequest;

@Sql("classpath:/InitializeTable.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

    }

    @Test
    void addInitialStationsTest() {
        InitialStationsAddRequest initialStationsAddRequest = new InitialStationsAddRequest(1L, 1L, 2L, 1);

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(initialStationsAddRequest)
            .when().post("/sections/initial")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    //TODO: 1L에는 없음 2L 아이디 호선에는 역 있음
    @ParameterizedTest
    @MethodSource("addInvalidRequest")
    void addInitialStationsTestFail(InitialStationsAddRequest initialStationsAddRequest) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(initialStationsAddRequest)
            .when().post("/sections/initial")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static Stream<Arguments> addInvalidRequest() {
        return Stream.of(
            Arguments.of(new InitialStationsAddRequest(0L, 1L, 2L, 1)),
            Arguments.of(new InitialStationsAddRequest(1L, 0L, 2L, 1)),
            Arguments.of(new InitialStationsAddRequest(1L, 1L, 0L, 1)),
            Arguments.of(new InitialStationsAddRequest(1L, 1L, 2L, 0)),
            Arguments.of(new InitialStationsAddRequest(2L, 1L, 2L, 1))
        );
    }
}
