package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.Direction.RIGHT;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;
import subway.repository.LineRepository;

@SuppressWarnings("NonAsciiCharacters")
public class StationControllerTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Nested
    class 성공_테스트 {

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

    @Nested
    class 역_등록시_예외_테스트 {

        @Test
        void 역을_등록할_때_노선명을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationSaveRequest request = new StationSaveRequest("", "B", "C", RIGHT, 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
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
        void 역을_등록할_때_기준역명을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationSaveRequest request = new StationSaveRequest("1호선", "", "C", RIGHT, 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("기준역명을 입력해주세요.")
            );
        }

        @Test
        void 역을_등록할_때_등록할_역명을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationSaveRequest request = new StationSaveRequest("1호선", "A", "", RIGHT, 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("등록할 역명을 입력해주세요.")
            );
        }

        @Test
        void 역을_등록할_때_방향을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationSaveRequest request = new StationSaveRequest("1호선", "B", "C", null, 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("왼쪽으로 등록할 경우 LEFT, 오른쪽으로 등록할 경우 RIGHT로 입력해주세요.")
            );
        }

        @Test
        void 역을_등록할_때_거리가_양수가_아닌_값을_입력하면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationSaveRequest request = new StationSaveRequest("1호선", "B", "C", RIGHT, 0);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("등록할 거리는 양수여야 합니다.")
            );
        }
    }

    @Nested
    class 역_삭제시_예외_테스트 {

        @Test
        void 역을_삭제할_때_노선명을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationDeleteRequest request = new StationDeleteRequest("", "A");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().delete("/stations")
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
        void 역을_삭제할_때_역명을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationDeleteRequest request = new StationDeleteRequest("1호선", "");

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().delete("/stations")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("역명을 입력해주세요.")
            );
        }
    }

    @Nested
    class 역_초기_등록시_예외_테스트 {

        @Test
        void 역을_초기등록할_때_노선명을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationInitialSaveRequest request = new StationInitialSaveRequest("", "B", "C", 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations/init")
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
        void 역을_초기등록할_때_시작역을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationInitialSaveRequest request = new StationInitialSaveRequest("1호선", "", "C", 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations/init")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("시작역을 입력해주세요.")
            );
        }

        @Test
        void 역을_초기등록할_때_도착역을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationInitialSaveRequest request = new StationInitialSaveRequest("1호선", "A", "", 3);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations/init")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("도착역을 입력해주세요.")
            );
        }

        @Test
        void 역을_등록할_때_거리가_양수가_아닌_값을_입력하면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED", List.of(
                    new Section("A", "B", 2)
            )));
            final StationInitialSaveRequest request = new StationInitialSaveRequest("1호선", "B", "A", 0);

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations/init")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getObject("message", String.class))
                            .isEqualTo("등록할 거리는 양수여야 합니다.")
            );
        }
    }
}
