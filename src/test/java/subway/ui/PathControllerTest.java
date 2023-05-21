package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
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
import subway.repository.LineRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Sql(value = "/deleteAll.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PathControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 성공_테스트 {

        @Test
        void 최단_경로를_조회한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .param("startStationName", "A")
                    .param("endStationName", "D")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/path")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(response.body().jsonPath().getList("path")).containsExactly("A", "B", "D"),
                    () -> assertThat(response.body().jsonPath().getInt("fare")).isEqualTo(1250)
            );
        }
    }

    @Nested
    class 예외_테스트 {

        @Test
        void 최단_경로를_조회시_출발역을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .param("endStationName", "D")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/path")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getString("message"))
                            .isEqualTo("최단 경로를 조회할 출발역과 도착역 모두 입력해주세요.")
            );
        }

        @Test
        void 최단_경로를_조회시_도착역을_입력하지_않으면_예외가_발생한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .param("startStationName", "D")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/path")
                    .then().log().all()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.body().jsonPath().getString("message"))
                            .isEqualTo("최단 경로를 조회할 출발역과 도착역 모두 입력해주세요.")
            );
        }
    }
}
