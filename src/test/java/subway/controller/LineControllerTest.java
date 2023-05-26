package subway.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.service.dto.request.RegisterLineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(value = "/deleteTable.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    LineDao lineDao;
    @Autowired
    SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("새로운 노선을 등록한다.")
    void registerLine() {

        //given
        RegisterLineRequest registerLineRequest = new RegisterLineRequest("A역", "B역", "1호선", 3);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerLineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/lines/1")
        );
    }

    @Test
    @DisplayName("특정 라인을 조회한다.")
    void searchLine() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        sectionDao.batchSave(List.of(new SectionEntity("A역", "B역", 2, 1L)));


        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/1")
                .then().log().all()
                .extract();

        String lineName = response.jsonPath().get("lineName");
        List<String> currentStations = response.jsonPath().getList("sectionInLineResponses.currentStationName");
        List<String> nextStations = response.jsonPath().getList("sectionInLineResponses.nextStationName");

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(lineName).isEqualTo("1호선"),
                () -> assertThat(currentStations).contains("A역"),
                () -> assertThat(nextStations).contains("B역")
        );
    }

    @Test
    @DisplayName("모든 라인을 조회한다.")
    void searchAllLines() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        lineDao.save(new LineEntity(2L, "2호선"));
        sectionDao.batchSave(List.of(new SectionEntity(1L, "A역", "B역", 2, 1L)));
        sectionDao.batchSave(List.of(new SectionEntity(1L, "Z역", "Y역", 2, 2L)));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        List<String> lineNames = response.jsonPath().getList("lineName");
        List<List<String>> currentStationNames = response.jsonPath().getList("sectionInLineResponses.currentStationName");
        List<List<String>> nextStationNames = response.jsonPath().getList("sectionInLineResponses.nextStationName");

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(lineNames).contains("1호선", "2호선"),
                () -> assertThat(currentStationNames).contains(List.of("A역"), List.of("Z역")),
                () -> assertThat(nextStationNames).contains(List.of("B역"), List.of("Y역"))
        );
    }

}