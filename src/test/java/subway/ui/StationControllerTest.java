package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.Direction.RIGHT;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;
import subway.repository.LineRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Sql(value = "/deleteAll.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 역을_추가한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", List.of(
                new Section("A", "B", 2)
        )));
        final StationSaveRequest request = new StationSaveRequest("1호선", "B", "C", RIGHT, 3);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();

        // then
        final List<Line> result = lineRepository.findAll();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).flatExtracting(Line::getSections).contains(
                        new Section("A", "B", 2),
                        new Section("B", "C", 3)
                )
        );
    }

    @Test
    void 역을_제거한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", List.of(
                new Section("A", "B", 2)
        )));
        final StationDeleteRequest request = new StationDeleteRequest("1호선", "B");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().delete("/stations")
                .then().log().all()
                .extract();

        // then
        final List<Line> result = lineRepository.findAll();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(result.get(0).getSections()).isEmpty()
        );
    }

    @Test
    void 노선의_역이_없을_때_역을_추가한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", Collections.emptyList()));
        final StationInitialSaveRequest request = new StationInitialSaveRequest("1호선", "A", "B", 3);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations/init")
                .then().log().all()
                .extract();

        // then
        final List<Line> result = lineRepository.findAll();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).flatExtracting(Line::getSections).contains(
                        new Section("A", "B", 3)
                )
        );
    }
}
