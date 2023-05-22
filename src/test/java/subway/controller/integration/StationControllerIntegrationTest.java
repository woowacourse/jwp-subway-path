package subway.controller.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import subway.dto.request.StationCreateRequest;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationsResponse;
import subway.service.StationService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class StationControllerIntegrationTest {

	@Autowired
	private StationService stationService;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("역 생성 테스트")
	void createStation() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
		long id = 1L;

		// when & then
		RestAssured
			.given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(stationCreateRequest)
			.when().post("/stations")
			.then()
			.statusCode(HttpStatus.CREATED.value())
			.header("location", "/stations/" + id);
	}

	@Test
	@DisplayName("모든 역 조회 테스트")
	void findAll() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
		stationService.saveStation(stationCreateRequest);

		// when & then
		RestAssured
			.given()
			.when().get("/stations")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("stations[0].id", equalTo(1))
			.body("stations[0].name", equalTo("잠실역"));
	}

	@Test
	@DisplayName("역 조회 테스트")
	void findById() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
		stationService.saveStation(stationCreateRequest);

		// when & then
		RestAssured
			.given()
			.when().get("/stations/1")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("id", equalTo(1))
			.body("name", equalTo("잠실역"));
	}

	@Test
	@DisplayName("역 삭제 테스트")
	void deleteStation() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
		stationService.saveStation(stationCreateRequest);

		// when & then
		RestAssured
			.given()
			.when().delete("/stations/1")
			.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		StationsResponse stations = stationService.findAllStationResponses();
		assertThat(stations.getStations().size()).isEqualTo(0);
	}

	@Test
	@DisplayName("역 갱신 테스트")
	void edit_station_success() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
		long id = stationService.saveStation(stationCreateRequest);

		StationUpdateRequest stationUpdateRequest = new StationUpdateRequest("신사역");

		// when & then
		RestAssured
			.given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(stationUpdateRequest)
			.when().patch("/stations/" + id)
			.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		StationsResponse stations = stationService.findAllStationResponses();
		assertThat(stations.getStations().get(0).getName()).isEqualTo(stationUpdateRequest.getName());
	}
}
