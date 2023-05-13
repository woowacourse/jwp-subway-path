package subway.service.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.subway.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.repository.StationRepository;
import subway.service.StationService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationServiceMockTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @Test
    @DisplayName("역을 저장한다.")
    void save_section_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");

        // when
        stationService.saveStation(stationCreateRequest);

        // then
        verify(stationRepository).insertStation(new Station(stationCreateRequest.getName()));
    }

    @Test
    @DisplayName("역을 찾는다.")
    void find_station_success() {
        // given
        Long id = 1L;
        Station station = new Station("잠실역");
        when(stationRepository.findByStationId(id)).thenReturn(station);

        // when
        StationResponse result = stationService.findStationEntityById(id);

        // then
        assertThat(result.getName()).isEqualTo(station.getName());
    }

    @Test
    @DisplayName("역을 모두 찾는다.")
    void find_stations_success() {
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
}
