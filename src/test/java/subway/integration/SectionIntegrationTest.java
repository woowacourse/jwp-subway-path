package subway.integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.ui.dto.LineRequest;
import subway.ui.dto.SectionRequest;
import subway.ui.dto.SectionResponse;

@DisplayName("지하철 구간 관련 기능")

public class SectionIntegrationTest extends IntegrationTest {

	private LineRequest lineRequest;
	private SectionRequest sectionRequest;

	@BeforeEach
	public void setUp() {
		super.setUp();
		lineRequest = new LineRequest("신분당선", "bg-red-600");
		sectionRequest = new SectionRequest("잠실역", "잠실새내역", 10);
	}

	@DisplayName("지하철 노선에 역을 추가한다.")
	@Test
	void addStationToLine() {
		// given
		final ExtractableResponse<Response> createResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(lineRequest)
			.when().post("/lines")
			.then().log().all().
			extract();

		final long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(sectionRequest)
			.when().post("/lines/{lineId}/stations", lineId)
			.then().log().all().
			extract();

		final SectionResponse sectionResponse = response.as(SectionResponse.class);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(sectionResponse.getDeparture().getName()).isEqualTo(sectionRequest.getDeparture());
		assertThat(sectionResponse.getArrival().getName()).isEqualTo(sectionRequest.getArrival());
		assertThat(sectionResponse.getDistance()).isEqualTo(sectionRequest.getDistance());
	}

	@DisplayName("지하철 노선에서 역을 제거한다.")
	@Test
	void removeStationAtLine() {
		// given
		final ExtractableResponse<Response> createResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(lineRequest)
			.when().post("/lines")
			.then().log().all().
			extract();

		final long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

		ExtractableResponse<Response> addResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(sectionRequest)
			.when().post("/lines/{lineId}/stations", lineId)
			.then().log().all().
			extract();

		final Long stationId = addResponse.as(SectionResponse.class).getDeparture().getId();

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
			.then().log().all().
			extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
