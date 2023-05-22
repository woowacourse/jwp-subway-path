package subway.adapter.in.web.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.adapter.out.persistence.repository.LineJdbcRepository;
import subway.common.IntegrationTest;
import subway.domain.Line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DeleteLineControllerTest extends IntegrationTest {

    @Autowired
    private LineJdbcRepository lineRepository;

    @Test
    @DisplayName("delete  /lines/{id} 노선을 삭제한다.")
    void deleteLine() {
        final Long lineId = lineRepository.createLine(new Line("1호선", 100));

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineRepository.findAll()).hasSize(0)
        );
    }
}