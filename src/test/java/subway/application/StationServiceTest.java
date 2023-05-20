package subway.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;

@SpringBootTest
@Transactional
class StationServiceTest {

	@Autowired
	StationService service;

	String jamsil;
	String samsung;
	StationUpdateRequest request;
	StationResponse response;

	@BeforeEach
	void setUp() {
		jamsil = "잠실";
		samsung = "삼성";
		request = new StationUpdateRequest(jamsil);
		response = service.createStation(request);

	}

	@DisplayName("역 생성 서비스 테스트")
	@Test
	void createStation() {
		// // then
		assertThat(response.getName()).isEqualTo(jamsil);
	}

	@DisplayName("전체 역 조회 서비스 테스트")
	@Test
	void findAll() {
		// given
		final StationUpdateRequest request2 = new StationUpdateRequest(samsung);
		service.createStation(request2);

		final List<StationResponse> stations = service.findAll();

		// then
		assertThat(stations).hasSize(2);
	}

	@DisplayName("ID를 사용한 역 조회 서비스 테스트")
	@Test
	void findById() {
		// given
		final long stationId = response.getId();
		final StationResponse foundStation = service.findById(stationId);

		// then
		assertThat(foundStation.getName()).isEqualTo(jamsil);
	}

	@DisplayName("역 갱신 서비스 테스트")
	@Test
	void updateStation() {
		// given
		final long stationId = response.getId();
		final StationUpdateRequest request2 = new StationUpdateRequest(samsung);

		// when
		final StationResponse response2 = service.updateStation(stationId, request2);

		// then
		assertThat(response2.getName()).isEqualTo(samsung);
	}

	@DisplayName("역 삭제 서비스 테스트")
	@Test
	void deleteById() {
		// when
		final long stationId = response.getId();
		final long deletedId = service.deleteById(stationId);

		// then
		Assertions.assertThat(deletedId).isEqualTo(stationId);
	}
}
