package subway.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.subway.Station;
import subway.dto.request.StationCreateRequest;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;
import subway.dto.response.StationsResponse;
import subway.exception.BlankNameException;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

	@InjectMocks
	private StationService stationService;

	@Mock
	private StationRepository stationRepository;

	@Test
	@DisplayName("역 생성 서비스 테스트")
	void createStation() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");

		// when
		stationService.saveStation(stationCreateRequest);

		// then
		verify(stationRepository).insertStation(new Station(stationCreateRequest.getName()));
	}

	@Test
	@DisplayName("역 이름이 공백이면 예외가 발생한다")
	void exception_wheBlankStationName() {
		// given
		StationCreateRequest stationCreateRequest = new StationCreateRequest("");

		// when & then
		assertThatThrownBy(() -> stationService.saveStation(stationCreateRequest))
			.isInstanceOf(BlankNameException.class);
	}

	@Test
	@DisplayName("역 id를 사용한 조회 서비스 테스트")
	void findById() {
		// given
		long id = 1L;
		Station station = new Station("잠실역");
		when(stationRepository.findByStationId(id)).thenReturn(station);

		// when
		StationResponse result = stationService.findStationResponseById(id);

		// then
		assertThat(result.getName()).isEqualTo(station.getName());
	}

	@Test
	@DisplayName("역 전체 조회 서비스 테스트")
	void findAll() {
		// given
		List<Station> stations = List.of(new Station("잠실역"));
		when(stationRepository.findAll()).thenReturn(stations);

		// when
		StationsResponse result = stationService.findAllStationResponses();

		// then
		assertAll(
			() -> assertThat(result.getStations().size()).isEqualTo(1),
			() -> assertThat(result.getStations().get(0).getName()).isEqualTo(stations.get(0).getName())
		);
	}

	@Test
	@DisplayName("역 갱신 서비스 테스트")
	void updateStation() {
		// given
		Long id = 1L;
		StationUpdateRequest stationUpdateRequest = new StationUpdateRequest("신사역");

		Station station = new Station(1, "잠실역");
		given(stationRepository.findByStationId(id)).willReturn(station);

		// when
		stationService.updateStation(id, stationUpdateRequest);

		// then
		verify(stationRepository).update(id, station);
	}

	@DisplayName("역 삭제 테스트")
	@Test
	void deleteStation() {
		// given
		long id = 1L;

		// when
		stationService.deleteStationById(id);

		// then
		verify(stationRepository).deleteById(id);
	}
}
