package subway.service.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.subway.Station;
import subway.dto.request.StationCreateRequest;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;
import subway.dto.response.StationsResponse;
import subway.exception.BlankNameException;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;
import subway.service.StationService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class StationServiceIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private StationService stationService;

	@Autowired
	private StationRepository stationRepository;

	@BeforeEach
	void setUp() {
		RestAssured.port = this.port;
	}

	@Test
	@DisplayName("역 생성 서비스 인수테스트")
	void createSection() {
		// given
		StationCreateRequest createRequest = new StationCreateRequest("잠실역");

		// when
		stationService.saveStation(createRequest);

		// then
		StationsResponse response = stationService.findAllStationResponses();

		assertAll(
			() -> assertThat(response.getStations().size()).isEqualTo(1),
			() -> assertThat(response.getStations().get(0).getName()).isEqualTo(createRequest.getName())
		);
	}

	@Test
	@DisplayName("역의 이름이 공백이면 예외가 발생한다")
	void exception_whenBlankStationName() {
		// given
		StationCreateRequest createRequest = new StationCreateRequest("");

		// then
		assertThatThrownBy(() -> stationService.saveStation(createRequest))
			.isInstanceOf(BlankNameException.class);
	}

	@Test
	@DisplayName("id로 역 조회 서비스 인수테스트")
	void findById() {
		// given
		Station station = new Station("잠실역");
		Long id = stationRepository.insertStation(station);

		// when
		StationResponse response = stationService.findStationResponseById(id);

		// then
		assertThat(response.getName()).isEqualTo(station.getName());
	}

	@Test
	@DisplayName("전체 역 조회 서비스 인수테스트")
	void findAll() {
		// given
		Station station = new Station("잠실역");
		stationRepository.insertStation(station);

		// when
		StationsResponse response = stationService.findAllStationResponses();

		// then
		assertAll(
			() -> assertThat(response.getStations().size()).isEqualTo(1),
			() -> assertThat(response.getStations().get(0).getName()).isEqualTo(station.getName())
		);
	}

	@Test
	@DisplayName("역 갱신 서비스 인수테스트")
	void updateStation() {
		// given
		StationCreateRequest createRequest = new StationCreateRequest("잠실역");
		long id = stationService.saveStation(createRequest);

		StationUpdateRequest updateRequest = new StationUpdateRequest("신사역");

		// when
		stationService.updateStation(id, updateRequest);

		// then
		StationsResponse response = stationService.findAllStationResponses();

		assertAll(
			() -> assertThat(response.getStations().size()).isEqualTo(1),
			() -> assertThat(response.getStations().get(0).getName()).isEqualTo(updateRequest.getName())
		);
	}

	@Test
	@DisplayName("존재하지 않은 역을 조회 시 예외가 발생한다")
	void exception_whenStationNotFound() {
		// given
		StationUpdateRequest updateRequest = new StationUpdateRequest("신사역");
		Long id = 1L;

		// then
		assertThatThrownBy(() -> stationService.updateStation(id, updateRequest))
			.isInstanceOf(StationNotFoundException.class);
	}

	@Test
	@DisplayName("역 삭제 서비스 인수테스트")
	void deleteStation() {
		// given
		StationCreateRequest createRequest = new StationCreateRequest("잠실역");
		long id = stationService.saveStation(createRequest);

		// when
		stationService.deleteStationById(id);

		// then
		StationsResponse expected = stationService.findAllStationResponses();

		assertThat(expected.getStations().size()).isEqualTo(0);
	}
}
