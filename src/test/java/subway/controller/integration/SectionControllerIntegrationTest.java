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
import subway.dto.request.LineCreateRequest;
import subway.dto.request.SectionCreateRequest;
import subway.dto.request.SectionDeleteRequest;
import subway.dto.request.StationCreateRequest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class SectionControllerIntegrationTest {

	@Autowired
	private StationService stationService;

	@Autowired
	private LineService lineService;

	@Autowired
	private SectionService sectionService;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("Section 생성 테스트")
	void createSection() {
		// given
		LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선");
		lineService.saveLine(lineCreateRequest);

		StationCreateRequest stationCreateRequest1 = new StationCreateRequest("잠실역");
		StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
		stationService.saveStation(stationCreateRequest1);
		stationService.saveStation(stationCreateRequest2);

		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3L);

		// when & then
		RestAssured
			.given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(sectionCreateRequest)
			.when().post("/sections")
			.then()
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	@DisplayName("Section 삭제 테스트")
	void deleteSection() {
		// given
		LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선");
		lineService.saveLine(lineCreateRequest);

		StationCreateRequest stationCreateRequest1 = new StationCreateRequest("잠실역");
		StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
		stationService.saveStation(stationCreateRequest1);
		stationService.saveStation(stationCreateRequest2);

		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3L);
		sectionService.insertSection(sectionCreateRequest);

		SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest("2호선", "잠실역");

		// when & then
		RestAssured
			.given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(sectionDeleteRequest)
			.when().delete("/sections")
			.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
