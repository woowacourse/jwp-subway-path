package subway.path.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.path.CostCalculator;
import subway.path.presentation.dto.response.PathResponse;
import subway.section.domain.Section;
import subway.section.domain.repository.SectionRepository;
import subway.station.domain.Station;
import subway.station.domain.repository.StationRepository;
import subway.vo.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    PathService pathService;

    @Mock
    CostCalculator costCalculator;

    @Mock
    SectionRepository sectionRepository;

    @Mock
    StationRepository stationRepository;

    @DisplayName("도착지부터 목적지까지의 최단 경로와 비용을 반환한다.")
    @Test
    void findByDijkstra() {
        // given
        Station 잠실역 = Station.of(1L, Name.from("잠실역"));
        Station 잠실새내역 = Station.of(2L, Name.from("잠실새내역"));
        Station 종합운동장역 = Station.of(3L, Name.from("종합운동장역"));
        Station 삼성역 = Station.of(4L, Name.from("삼성역"));

        doReturn(잠실역).when(stationRepository).findById(1L);
        doReturn(종합운동장역).when(stationRepository).findById(3L);

        List<Station> stations = List.of(잠실역, 잠실새내역, 종합운동장역, 삼성역);
        doReturn(stations).when(stationRepository).findAll();

        List<Section> sections = List.of(
                Section.of(1L, 잠실역, 잠실새내역, 2),
                Section.of(1L, 잠실새내역, 종합운동장역, 3),
                Section.of(2L, 잠실역, 삼성역, 4),
                Section.of(2L, 삼성역, 종합운동장역, 5)
        );
        doReturn(sections).when(sectionRepository).findAll();

        when(costCalculator.calculateAdult(5)).thenReturn(1250);

        // when
        PathResponse response = pathService.findByDijkstra(1L, 3L);

        // then
        assertAll(
                () -> assertThat(response.getStations()).hasSize(3),
                () -> assertThat(response.getCost().intValue()).isEqualTo(1250),
                () -> assertThat(response.getDistance()).isEqualTo(5)
        );

    }

}
