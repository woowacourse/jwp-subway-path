package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionDeleteRequest;
import subway.section.dto.SectionResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void createSection() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 1L, 2L, true, 3);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList("data.", SectionResponse.class);
        Assertions.assertAll(
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getLineId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getUpStationId()).isEqualTo(2L),
                () -> assertThat(result.get(0).getDownStationId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getDistance()).isEqualTo(3)
        );
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        final SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest(1L, 1L);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionDeleteRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
