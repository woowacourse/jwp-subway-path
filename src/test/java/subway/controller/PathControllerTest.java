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
import subway.dto.LineDto;
import subway.dto.SectionDto;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.repository.LineRepository;

public class PathControllerTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    public void 최단_경로를_조회한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2),
                new Section("B", "C", 100)
        )));
        lineRepository.save(new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 2),
                new Section("Y", "Z", 5),
                new Section("C", "Y", 3)
        )));
        final ShortestPathRequest request = new ShortestPathRequest("C", "A", 17);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("start", request.getStart())
                .queryParam("end", request.getEnd())
                .queryParam("age", request.getAge())
                .when().get("/shortest-path")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getObject(".", ShortestPathResponse.class))
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(
                                new ShortestPathResponse(List.of(
                                        new LineDto(
                                                "2호선", "BLUE", List.of(
                                                new SectionDto("C", "Y", 3),
                                                new SectionDto("Y", "Z", 5),
                                                new SectionDto("Z", "B", 2)
                                        )),
                                        new LineDto(
                                                "1호선", "RED", List.of(
                                                new SectionDto("A", "B", 2)
                                        ))
                                ), 12, 800)
                        )
        );
    }
}
