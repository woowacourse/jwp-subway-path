package subway.service.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import subway.domain.subway.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationEditRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.event.RouteUpdateEvent;
import subway.exception.NameIsBlankException;
import subway.repository.StationRepository;
import subway.service.StationService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationServiceUnitTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private ApplicationEventPublisher publisher;

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
    @DisplayName("역의 이름이 공백이면 예외를 발생시킨다.")
    void throws_exception_when_station_name_is_blank() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("");

        // when & then
        assertThatThrownBy(() -> stationService.saveStation(stationCreateRequest))
                .isInstanceOf(NameIsBlankException.class);
    }

    @Test
    @DisplayName("역을 찾는다.")
    void find_station_success() {
        // given
        long id = 1L;
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

    @Test
    @DisplayName("역을 수정한다.")
    void edit_station_success() {
        // given
        Long id = 1L;
        StationEditRequest stationEditRequest = new StationEditRequest("판교역");

        Station station = new Station(1, "잠실역");
        given(stationRepository.findByStationId(id)).willReturn(station);

        // when
        stationService.editStation(id, stationEditRequest);

        // then
        verify(stationRepository).update(id, station);
        verify(publisher).publishEvent(any(RouteUpdateEvent.class));
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void delete_station_success() {
        // given
        long id = 1L;

        // when
        stationService.deleteStationById(id);

        // then
        verify(stationRepository).deleteById(id);
        verify(publisher).publishEvent(any(RouteUpdateEvent.class));
    }
}

