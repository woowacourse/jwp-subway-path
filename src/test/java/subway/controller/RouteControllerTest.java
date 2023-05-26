package subway.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@Sql(value = "/deleteTable.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteControllerTest {

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

    /**
     * 노선: (1호선) A -> B -> C -> D / (2호선) C -> E -> F
     * 출발역: A
     * 도착역: F
     * 최단 경로: A -> B -> C -> E -> F
     */
    @Test
    @DisplayName("두 역 사이의 최단 경로를 조회한다.")
    void findShortestPath() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        lineDao.save(new LineEntity(2L, "2호선"));
        sectionDao.batchSave(
                List.of(new SectionEntity("A역", "B역", 2, 1L),
                        new SectionEntity("B역", "C역", 3, 1L),
                        new SectionEntity("C역", "D역", 2, 1L)));
        sectionDao.batchSave(
                List.of(new SectionEntity("C역", "E역", 1, 2L),
                        new SectionEntity("E역", "F역", 4, 2L)));

        String startStation = "A역";
        String endStation = "F역";

        //when & then
        RestAssured.given().log().all()
                .param("startStation", startStation)
                .param("endStation", endStation)
                .when().get("/routes")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("route", response -> equalTo(List.of("A역", "B역", "C역", "E역", "F역")));
    }

    @Test
    @DisplayName("두 역 사이의 최단 경로를 거리를 조회한다.")
    void findShortestDistance() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        lineDao.save(new LineEntity(2L, "2호선"));
        sectionDao.batchSave(
                List.of(new SectionEntity("A역", "B역", 2, 1L),
                        new SectionEntity("B역", "C역", 3, 1L),
                        new SectionEntity("C역", "D역", 2, 1L)));
        sectionDao.batchSave(
                List.of(new SectionEntity("C역", "E역", 3, 2L),
                        new SectionEntity("E역", "F역", 4, 2L)));

        String startStation = "A역";
        String endStation = "F역";
        Float expectedDistance = 12.0F;

        //when & then
        RestAssured.given().log().all()
                .param("startStation", startStation)
                .param("endStation", endStation)
                .when().get("/routes")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("distance", response -> equalTo(expectedDistance));
    }

    @Test
    @DisplayName("두 역 사이의 요금을 조회한다.")
    void findFare() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        lineDao.save(new LineEntity(2L, "2호선"));
        sectionDao.batchSave(
                List.of(new SectionEntity("A역", "B역", 2, 1L),
                        new SectionEntity("B역", "C역", 3, 1L),
                        new SectionEntity("C역", "D역", 2, 1L)));
        sectionDao.batchSave(
                List.of(new SectionEntity("C역", "E역", 3, 2L),
                        new SectionEntity("E역", "F역", 4, 2L)));

        String startStation = "A역";
        String endStation = "F역";
        int expectedFare = 1350;

        //when & then
        RestAssured.given().log().all()
                .param("startStation", startStation)
                .param("endStation", endStation)
                .when().get("/routes")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("fare", response -> equalTo(expectedFare));
    }

}