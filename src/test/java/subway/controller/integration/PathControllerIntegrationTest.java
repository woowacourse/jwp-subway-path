package subway.controller.integration;

import io.restassured.RestAssured;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import subway.dto.request.LineCreateRequest;
import subway.dto.request.PathRequest;
import subway.dto.request.SectionCreateRequest;
import subway.dto.request.StationCreateRequest;
import subway.IntegrationTest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class PathControllerIntegrationTest extends IntegrationTest {

	@Autowired
	private LineService lineService;

	@Autowired
	private StationService stationService;

	@Autowired
	private SectionService sectionService;

	@Test
	@DisplayName("최단 경로 조회 테스트")
	void findShortestPath() {
		// given
		PathRequest request = new PathRequest("잠실역", "선릉역");
		LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선");
		lineService.saveLine(lineCreateRequest);

		StationCreateRequest stationCreateRequest1 = new StationCreateRequest("잠실역");
		StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
		StationCreateRequest stationCreateRequest3 = new StationCreateRequest("선릉역");
		stationService.saveStation(stationCreateRequest1);
		stationService.saveStation(stationCreateRequest2);
		stationService.saveStation(stationCreateRequest3);

		SectionCreateRequest sectionCreateRequest1 = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		SectionCreateRequest sectionCreateRequest2 = new SectionCreateRequest("2호선", "잠실새내역", "선릉역", 5);
		sectionService.insertSection(sectionCreateRequest1);
		sectionService.insertSection(sectionCreateRequest2);

		// when & then
		RestAssured
			.given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().get("/paths")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("paths[0].station.name", equalTo("잠실역"))
			.body("paths[0].lineNames[0]", equalTo("2호선"))
			.body("paths[1].station.name", equalTo("잠실새내역"))
			.body("paths[1].lineNames[0]", equalTo("2호선"))
			.body("paths[2].station.name", equalTo("선릉역"))
			.body("paths[2].lineNames[0]", equalTo("2호선"));
	}
}
