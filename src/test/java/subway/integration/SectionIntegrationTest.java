package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.section.dto.SectionRequest;

public class SectionIntegrationTest extends IntegrationTest {

    private SectionRequest sectionRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        sectionRequest = new SectionRequest(1L, 2L, 3L, true, 3);
    }

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void createSection() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
