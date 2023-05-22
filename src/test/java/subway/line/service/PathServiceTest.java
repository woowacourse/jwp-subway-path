package subway.line.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.ShortestPathRequest;
import subway.line.dto.ShortestPathResponse;
import subway.line.dto.TraverseStationDto;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

  private static final Station 잠실새내역 = new Station(2L, "잠실새내역");
  private static final Station 잠실역 = new Station(1L, "잠실역");
  private static final Station 잠실나루역 = new Station(3L, "잠실나루역");

  @Mock
  private LineRepository lineRepository;

  @Mock
  private StationRepository stationRepository;

  @InjectMocks
  private PathService pathService;

  @Test
  void findShortestPath() {
    given(stationRepository.findById(잠실새내역.getId())).willReturn(잠실새내역);
    given(stationRepository.findById(잠실역.getId())).willReturn(잠실역);

    given(lineRepository.findAll()).willReturn(List.of(
        new Line(1L, "1호선", Sections.values(
            List.of(
                Section.of(잠실새내역, 잠실나루역, 50)
            )
        )),
        new Line(2L, "2호선", Sections.values(
            List.of(
                Section.of(잠실새내역, 잠실역, 100)
            )
        )),
        new Line(3L, "3호선", Sections.values(
            List.of(
                Section.of(잠실나루역, 잠실역, 8)
            )
        ))
    ));

    final ShortestPathResponse shortestPath = pathService.findShortestPath(
        new ShortestPathRequest(잠실새내역.getId(), 잠실역.getId()));

    final List<TraverseStationDto> traverseStationDtos = shortestPath.getTraverseStationDtos();
    assertAll(
        () -> assertThat(shortestPath.getDistance()).isEqualTo(58),
        () -> assertThat(shortestPath.getFare()).isEqualTo(2150),
        () -> assertThat(traverseStationDtos.get(0).getLineName()).isEqualTo("1호선"),
        () -> assertThat(traverseStationDtos.get(0).getStationName()).isEqualTo("잠실새내역"),
        () -> assertThat(traverseStationDtos.get(1).getLineName()).isEqualTo("3호선"),
        () -> assertThat(traverseStationDtos.get(1).getStationName()).isEqualTo("잠실나루역"),
        () -> assertThat(traverseStationDtos.get(2).getLineName()).isEqualTo("3호선"),
        () -> assertThat(traverseStationDtos.get(2).getStationName()).isEqualTo("잠실역")
    );
  }
}
