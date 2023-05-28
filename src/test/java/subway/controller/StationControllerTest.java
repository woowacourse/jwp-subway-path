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
import subway.service.dto.request.StationRegisterRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "/deleteTable.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationControllerTest {

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
    @DisplayName("특정 라인에 새로운 노선을 추가한다.")
    void registerStationInLine() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        StationRegisterRequest stationRegisterRequest = new StationRegisterRequest("1호선", "A역", "B역", 3);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRegisterRequest)
                .when().post("/stations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 라인에 포함된 특정 역을 삭제한다.")
    void deleteStationInline() {

        //given
        lineDao.save(new LineEntity(1L, "1호선"));
        sectionDao.batchSave(List.of(new SectionEntity(1L, "A역", "B역", 2, 1L)));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/stations/1/A역")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}