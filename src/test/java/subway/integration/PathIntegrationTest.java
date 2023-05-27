package subway.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.ui.dto.LineRequest;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;
import subway.ui.dto.SectionRequest;
import subway.ui.dto.StationRequest;

public class PathIntegrationTest extends IntegrationTest {

	private PathRequest pathRequest;

	@BeforeEach
	public void setUp() {
		super.setUp();
		pathRequest = new PathRequest("잠실역", "선정릉역");
	}

	@Test
	@DisplayName("최단 거리 경로를 찾는다.")
	void findShortestPathTest() {
		//given
		createLines();

		//when
		final ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(pathRequest)
			.when().get("/paths")
			.then().log().all()
			.extract();

		final PathResponse pathResponse = response.as(PathResponse.class);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(pathResponse.getDistance()).isEqualTo(4 + 2 + 7 + 5);
		assertThat(pathResponse.getFee()).isEqualTo(1450);

	}

	private void createLines() {
		final long line2Id = createLine("2호선", "green");
		final long line9Id = createLine("9호선", "yellow");

		createStations(List.of("잠실역", "잠실새내역", "종합운동장역", "삼성역", "선릉역", "선정릉역", "봉은사역", "석촌역", "올림픽공원역"));

		createSections(line2Id, List.of(
			new SectionRequest("잠실역", "잠실새내역", 4),
			new SectionRequest("잠실새내역", "종합운동장역", 2),
			new SectionRequest("종합운동장역", "삼성역", 3),
			new SectionRequest("삼성역", "선릉역", 8)
		));

		createSections(line9Id, List.of(
			new SectionRequest("선정릉역", "봉은사역", 7),
			new SectionRequest("봉은사역", "종합운동장역", 5),
			new SectionRequest("종합운동장역", "석촌역", 6),
			new SectionRequest("석촌역", "올림픽공원역", 10)
		));
	}

	private long createLine(final String name, final String color) {
		ExtractableResponse<Response> createLineResponse = RestAssured
			.given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new LineRequest(name, color))
			.when().post("/lines")
			.then()
			.extract();

		return Long.parseLong(createLineResponse.header("Location").split("/")[2]);
	}

	private void createStations(final List<String> stationNames) {
		for (String stationName : stationNames) {
			RestAssured
				.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new StationRequest(stationName))
				.when().post("/stations")
				.then()
				.extract();
		}
	}

	private void createSections(final long lineId, final List<SectionRequest> sectionRequests) {
		for (SectionRequest sectionRequest : sectionRequests) {
			RestAssured
				.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(sectionRequest)
				.when().post("/lines/{lineId}/stations", lineId)
				.then()
				.extract();
		}
	}

}
