package subway.ui.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.IntegrationTest;
import subway.domain.Line;
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.adapter.in.web.line.dto.LineRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateLineControllerTest extends IntegrationTest {

    @Autowired
    private LineJdbcAdapter lineRepository;

    @Test
    @DisplayName("post /lines  노선을 추가한다.")
    void createLine() {
        final LineRequest lineRequest = new LineRequest("1호선");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(lineRepository.findAll()).usingRecursiveComparison().ignoringFields("id").isEqualTo(List.of(new Line(1L, "1호선"))
                ));
    }
}