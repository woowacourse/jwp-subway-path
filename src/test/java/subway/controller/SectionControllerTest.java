package subway.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionResponse;

class SectionControllerTest extends ControllerTest {

    @Autowired
    private LineDao lineDao;
    @Autowired
    private StationDao stationDao;

    private Long lineId;
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineId = lineDao.insert(new Line("2호선", "초록색")).getId();

        stationId1 = stationDao.insert(new Station("잠실역")).getId();
        stationId2 = stationDao.insert(new Station("선릉역")).getId();
        stationId3 = stationDao.insert(new Station("사당역")).getId();
    }

    @DisplayName("빈 노선에 역을 등록한다.")
    @Test
    void createSectionToEmptyLine() {
        // given
        final SectionCreateRequest request = new SectionCreateRequest(lineId, stationId1, stationId2, true, 3);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
        Assertions.assertAll(
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getLineId()).isEqualTo(lineId),
                () -> assertThat(result.get(0).getUpStationId()).isEqualTo(stationId2),
                () -> assertThat(result.get(0).getDownStationId()).isEqualTo(stationId1),
                () -> assertThat(result.get(0).getDistance()).isEqualTo(3)
        );
    }

    @DisplayName("baseId에 해당하는 역이 포함된 구간이 없으면 예외를 발생시킨다.")
    @Test
    void createSectionWhenBaseIdNoExist() {
        // given
        final SectionCreateRequest request1 = new SectionCreateRequest(lineId, stationId1, stationId2, false, 3);
        final SectionCreateRequest badRequest = new SectionCreateRequest(lineId, stationId3, stationId2, true, 3);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request1)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(badRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        final SectionCreateRequest createRequest = new SectionCreateRequest(lineId, stationId1, stationId2, true, 3);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();

        final SectionDeleteRequest request = new SectionDeleteRequest(lineId, stationId1);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
