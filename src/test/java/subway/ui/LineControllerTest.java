package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineUpdateRequest;
import subway.repository.LineRepository;

@SuppressWarnings("NonAsciiCharacters")
public class LineControllerTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Nested
    class 성공_테스트 {
        @Test
        void 노선을_추가한다() {
            // given
            final LineSaveRequest request = new LineSaveRequest("1호선", "RED");

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
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final Long id = lineRepository.findIdByName("1호선");

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
                            .isEqualTo(new LineResponse("1호선", "RED", List.of("A", "B")))
            );
        }

        @Test
        void 노선을_전체_조회한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("B", "C", 3),
                    new Section("A", "B", 2),
                    new Section("D", "E", 5),
                    new Section("C", "D", 4)
            )));
            lineRepository.save(new Line("2호선", "BLUE", List.of(
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
                                    new LineResponse("1호선", "RED", List.of("A", "B", "C", "D", "E")),
                                    new LineResponse("2호선", "BLUE", List.of("Z", "B", "Y"))
                            ))
            );
        }

        @Test
        void 노선을_수정한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final Long id = lineRepository.findIdByName("1호선");
            final LineUpdateRequest request = new LineUpdateRequest("2호선", "BLUE");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().put("/lines/" + id)
                    .then().log().all()
                    .extract();

            // then
            final Line line = lineRepository.findById(id);
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                    () -> assertThat(line.getName()).isEqualTo("2호선"),
                    () -> assertThat(line.getColor()).isEqualTo("BLUE")
            );
        }

        @Test
        void 노선을_제거한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final Long id = lineRepository.findIdByName("1호선");

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

    @Nested
    class 예외_테스트 {
        @Test
        void 존재하지_않는_id의_노선을_조회할_경우_예외를_던진다() {
            // given
            final int nonExistId = 1;

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/lines/" + nonExistId)
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("노선을 찾을 수 없습니다.")
            );
        }

        @Test
        void 존재하지_않는_id의_노선을_수정할_경우_예외를_던진다() {
            // given
            final int nonExistId = 1;
            final LineUpdateRequest request = new LineUpdateRequest("2호선", "BLUE");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().put("/lines/" + nonExistId)
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("노선을 찾을 수 없습니다.")
            );
        }

        @Test
        void 존재하지_않는_id의_노선을_삭제할_경우_예외를_던진다() {
            // given
            final int nonExistId = 1;

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/lines/" + nonExistId)
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("노선을 찾을 수 없습니다.")
            );
        }

        @Test
        void 노선_추가시_노선_이름을_입력하지_않으면_예외가_발생한다() {
            // given
            final LineSaveRequest request = new LineSaveRequest("", "RED");

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
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("노선명을 입력해주세요.")

            );
        }

        @Test
        void 노선_추가시_노선_색을_입력하지_않으면_예외가_발생한다() {
            // given
            final LineSaveRequest request = new LineSaveRequest("1호선", "");

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
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("노선 색을 입력해주세요.")

            );
        }
    }
}
