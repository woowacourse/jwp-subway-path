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
import subway.service.dto.request.RouteFindingRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("두 역 사이의 최단 경로를 조회한다.")
    void findShortestPath() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        sectionDao.batchSave(List.of(new SectionEntity("A역", "B역", 2, 1L)));

        RouteFindingRequest routeFindingRequest = new RouteFindingRequest("A역", "B역");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(routeFindingRequest)
                .when().get("/routes")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}