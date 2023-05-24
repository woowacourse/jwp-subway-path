package subway.line.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineCreateDto;
import subway.line.dto.LineResponseDto;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.dto.SectionCreateDto;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationResponseDto;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

  private static final Station 잠실새내역 = new Station(2L, "잠실새내역");
  private static final Station 잠실역 = new Station(1L, "잠실역");
  private static final Station 잠실나루역 = new Station(3L, "잠실나루역");

  @Mock
  private LineRepository lineRepository;

  @Mock
  private StationRepository stationRepository;

  @InjectMocks
  private LineService lineService;

  @Test
  void createLine() {
    final Line expectedLine = new Line(1L, "2호선");
    given(lineRepository.createLine(any())).willReturn(expectedLine);

    final Long actualId = lineService.createLine(new LineCreateDto(expectedLine.getLineName()));

    assertThat(actualId).isEqualTo(expectedLine.getId());
  }

  @Test
  void addSection() {
    final Line line = new Line(1L, "2호선");
    given(lineRepository.findById(line.getId())).willReturn(line);
    given(stationRepository.findById(잠실역.getId())).willReturn(잠실역);
    given(stationRepository.findById(잠실새내역.getId())).willReturn(잠실새내역);

    final Long actualId = lineService.addSection(new SectionCreateDto(line.getId(), 잠실새내역.getId(),
        잠실역.getId(), 5));

    assertThat(actualId).isEqualTo(line.getId());
  }

  @Test
  void findSortedStations() {
    final Line line = new Line(1L, "2호선", Sections.values(
        List.of(
        Section.of(잠실새내역, 잠실역, 5),
        Section.of(잠실역, 잠실나루역, 4)
        )));
    given(lineRepository.findById(line.getId())).willReturn(line);

    final List<StationResponseDto> sortedStations = lineService.findSortedStations(line.getId());

    final StationResponseDto firstStationDto = sortedStations.get(0);
    final StationResponseDto secondStationDto = sortedStations.get(1);
    final StationResponseDto thirdStationDto = sortedStations.get(2);

    assertAll(
        () -> assertThat(firstStationDto.getId()).isEqualTo(잠실새내역.getId()),
        () -> assertThat(firstStationDto.getName()).isEqualTo(잠실새내역.getName()),
        () -> assertThat(secondStationDto.getId()).isEqualTo(잠실역.getId()),
        () -> assertThat(secondStationDto.getName()).isEqualTo(잠실역.getName()),
        () -> assertThat(thirdStationDto.getId()).isEqualTo(잠실나루역.getId()),
        () -> assertThat(thirdStationDto.getName()).isEqualTo(잠실나루역.getName())
    );
  }

  @Test
  void findAllLines() {
    final Line expectedLine1 = new Line(1L, "2호선", Sections.values(
        List.of(
            Section.of(잠실새내역, 잠실역, 5)
        )
    ));
    final Line expectedLine2 = new Line(2L, "3호선", Sections.values(
        List.of(
            Section.of(잠실역, 잠실나루역, 4)
        )
    ));
    given(lineRepository.findAll()).willReturn(List.of(expectedLine1, expectedLine2));
    given(lineRepository.findById(expectedLine1.getId())).willReturn(expectedLine1);
    given(lineRepository.findById(expectedLine2.getId())).willReturn(expectedLine2);

    final List<LineResponseDto> lineResponseDtos = lineService.findAllLines();

    final LineResponseDto lineResponseDto1 = lineResponseDtos.get(0);
    final LineResponseDto lineResponseDto2 = lineResponseDtos.get(1);
    assertAll(
        () -> assertThat(lineResponseDtos.size()).isEqualTo(2),
        () -> assertThat(lineResponseDto1.getStationResponseDtos()).hasSize(2),
        () -> assertThat(lineResponseDto2.getStationResponseDtos()).hasSize(2)
    );
  }

  @Test
  void removeStationBy() {
    final Line line = new Line(1L, "2호선", Sections.values(
        List.of(
          Section.of(잠실새내역, 잠실역, 5)
        )
    ));
    given(lineRepository.findById(line.getId())).willReturn(line);
    given(stationRepository.findById(any())).willReturn(잠실역);

    lineService.removeStationBy(line.getId(), 잠실역.getId());

    assertThat(line.getSections()).isEmpty();
    verify(lineRepository, times(1)).updateLine(line);
  }
}
