package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.section.dto.SectionCreateControllerRequest;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.service.line.domain.Line;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.LineFixture.SECOND_LINE;
import static subway.domain.StationFixture.GANGNAM;
import static subway.domain.StationFixture.JAMSIL;
import static subway.domain.StationFixture.SEONLEUNG;
import static subway.domain.StationFixture.YUKSAM;

@SuppressWarnings("NonAsciiCharacters")
public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    StationDao stationDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 노선에_아무것도_없는_경우_섹션_추가() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                savedUpStation.getId(),
                savedDownStation.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 동일한_역_2개를_저장_입력으로_작성하면_예외() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);

        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                savedUpStation.getId(),
                savedUpStation.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        JsonPath responseBody = response.body().jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody.getString("message")).isEqualTo("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
    }

    @Test
    void 노선에_역이_있을_때_입력으로_들어온_역이_모두_노선에_존재하지_않으면_예외() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station gangnam = stationDao.insert(GANGNAM);
        Station seonleung = stationDao.insert(SEONLEUNG);
        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                gangnam.getId(),
                seonleung.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        JsonPath responseBody = response.body().jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody.getString("message")).isEqualTo("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");
    }

    @Test
    void 추가하려는_역이_모두_노선에_있으면_예외() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                savedUpStation.getId(),
                savedDownStation.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        JsonPath responseBody = response.body().jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody.getString("message")).isEqualTo("추가하려는 경로의 역들은 이미 노선에 존재하는 역들입니다.");
    }

    @Test
    void 추가하려는_경로의_길이가_기존_경로의_길이보다_크면_예외() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station gangnam = stationDao.insert(GANGNAM);
        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                savedUpStation.getId(),
                gangnam.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        JsonPath responseBody = response.body().jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody.getString("message")).isEqualTo("새로운 경로의 거리는 기존 경로보다 클 수 없습니다.");
    }

    @Test
    void 하행종점_추가() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station gangnam = stationDao.insert(GANGNAM);
        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                savedDownStation.getId(),
                gangnam.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상행종점_추가() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(SEONLEUNG);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station jamsil = stationDao.insert(JAMSIL);
        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                jamsil.getId(),
                savedUpStation.getId(),
                10,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상행역이_존재하고_하행역을_중간에_추가() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station seonleung = stationDao.insert(SEONLEUNG);
        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                savedUpStation.getId(),
                seonleung.getId(),
                5,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 하행역이_존재하고_상행역을_중간에_추가() throws JsonProcessingException {
        Line savedLine = lineDao.insert(SECOND_LINE);
        Station savedUpStation = stationDao.insert(JAMSIL);
        Station savedDownStation = stationDao.insert(YUKSAM);

        SectionEntity sectionEntity = new SectionEntity(savedUpStation.getId(), savedDownStation.getId(), 10, savedLine.getId());
        sectionDao.insert(sectionEntity);

        Station seonleung = stationDao.insert(SEONLEUNG);
        SectionCreateControllerRequest requestParam = new SectionCreateControllerRequest(
                seonleung.getId(),
                savedDownStation.getId(),
                5,
                savedLine.getId()
        );
        String jsonRequestParam = objectMapper.writeValueAsString(requestParam);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(jsonRequestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
