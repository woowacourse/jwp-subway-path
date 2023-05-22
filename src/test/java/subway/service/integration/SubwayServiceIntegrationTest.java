package subway.service.integration;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.SectionsFixture.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import subway.domain.subway.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.LineStationResponse;
import subway.dto.response.PathsResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.service.SubwayService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class SubwayServiceIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private SubwayService subwayService;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private LineRepository lineRepository;

	@BeforeEach
	void setUp() {
		RestAssured.port = this.port;
	}

	@Test
	@DisplayName("역을 순서대로 정렬한다")
	void sortStations() {
		// given
		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("선릉역"));

		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		// when
		LineStationResponse result = subwayService.findStationsByLineName("2호선");

		// then
		assertAll(
			() -> assertThat(result.getStations().size()).isEqualTo(3),
			() -> assertThat(result.getStations().get(0).getName()).isEqualTo("잠실역"),
			() -> assertThat(result.getStations().get(2).getName()).isEqualTo("선릉역")
		);
	}

	@Test
	@DisplayName("최단 경로를 조회한다")
	void returns_shortest_path() {
		// given
		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("선릉역"));
		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		PathRequest req = new PathRequest("잠실역", "선릉역");

		// when
		PathsResponse result = subwayService.findShortestPath(req);

		// then
		assertAll(
			() -> assertThat(result.getPaths().size()).isEqualTo(3),
			() -> assertThat(result.getPaths().get(0).getStation().getName()).isEqualTo("잠실역"),
			() -> assertThat(result.getPaths().get(2).getStation().getName()).isEqualTo("선릉역")
		);
	}
}
