package subway.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.IntegrationTest;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.dto.LineAddRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.repository.LineRepository;

@SuppressWarnings("NonAsciiCharacters")
public class LineControllerTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 노선을_추가한다() {
        // given
        final LineAddRequest request = new LineAddRequest("1호선", "RED", 0);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @Test
    void 노선id를_입력받아_노선을_조회한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2)
        )));
        final Long id = lineRepository.findIdByName("1호선").orElseThrow();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getObject(".", LineResponse.class)).usingRecursiveComparison()
                        .isEqualTo(new LineResponse("1호선", "RED", 0, List.of("A", "B")))
        );
    }

    @Test
    void 노선을_전체_조회한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        )));
        lineRepository.save(new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 3),
                new Section("B", "Y", 2)
        )));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList(".", LineResponse.class)).usingRecursiveComparison()
                        .isEqualTo(List.of(
                                new LineResponse("1호선", "RED", 0, List.of("A", "B", "C", "D", "E")),
                                new LineResponse("2호선", "BLUE", 0, List.of("Z", "B", "Y"))
                        ))
        );
    }

    @Test
    void 노선을_수정한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2)
        )));
        final Long id = lineRepository.findIdByName("1호선").orElseThrow();
        final LineUpdateRequest request = new LineUpdateRequest("2호선", "BLUE");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        final Line line = lineRepository.findById(id).orElseThrow();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(line.getName()).isEqualTo("2호선"),
                () -> assertThat(line.getColor()).isEqualTo("BLUE")
        );
    }

    @Test
    void 노선을_제거한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2)
        )));
        final Long id = lineRepository.findIdByName("1호선").orElseThrow();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        final List<Line> result = lineRepository.findAll();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(result).isEmpty()
        );
    }
}
