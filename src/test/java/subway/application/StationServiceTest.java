package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.StationInitRequest;
import subway.dto.StationInitResponse;
import subway.exception.line.LineIsNotInitException;
import subway.repository.LineRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private LineRepository lineRepository;

    @Test
    @DisplayName("초기 두 역을 노선에 등록한다.")
    void register_init_station_to_line() {
        // given
        Line line = new Line(1L, "2호선", "#123456", new ArrayList<>());
        doReturn(line).when(lineRepository).findByName((any(String.class)));

        List<Station> stations = List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역"));
        doReturn(stations).when(lineRepository).saveInitStations(any(Section.class), any(Long.class));

        StationInitRequest request = new StationInitRequest("2호선", "잠실역", "선릉역", 10);

        // when
        StationInitResponse result = stationService.saveInitStations(request);

        // then
        assertThat(result.getUpboundStationId()).isEqualTo(stations.get(0).getId());
        assertThat(result.getUpboundStationName()).isEqualTo(stations.get(0).getName());
        assertThat(result.getDownboundStationId()).isEqualTo(stations.get(1).getId());
        assertThat(result.getDownboundStationName()).isEqualTo(stations.get(1).getName());
    }

    @Test
    @DisplayName("초기 역을 등록하는데 노선의 구간이 비어있지 않으면 에러를 발생한다.")
    void check_exception_when_register_init_station_to_not_init_station() {
        // given
        Line line = new Line(
            1L,
            "2호선",
            "#123456", List.of(new Section(new Station("잠실"), new Station("선릉"), 10)));
        doReturn(line).when(lineRepository).findByName((any(String.class)));

        StationInitRequest request = new StationInitRequest("2호선", "잠실역", "선릉역", 10);

        // when + then
        assertThatThrownBy(() -> stationService.saveInitStations(request))
                .isInstanceOf(LineIsNotInitException.class);

    }

}