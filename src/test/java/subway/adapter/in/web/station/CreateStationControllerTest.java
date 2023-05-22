package subway.adapter.in.web.station;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.adapter.in.web.station.dto.StationCreateRequest;
import subway.adapter.out.persistence.repository.StationJdbcRepository;
import subway.common.IntegrationTest;
import subway.domain.Station;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CreateStationControllerTest extends IntegrationTest {
    @Autowired
    private StationJdbcRepository stationRepository;

    @Test
    @DisplayName("post /stations  역을 등록한다.")
    void createStation() {
        StationCreateRequest 라빈 = new StationCreateRequest("라빈");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(라빈)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(stationRepository.findByName(new Station("라빈")).get()).usingRecursiveComparison().isEqualTo(new Station("라빈"))
        );
    }
}
