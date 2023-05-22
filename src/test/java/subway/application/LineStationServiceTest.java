package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.*;
import subway.dto.LineStationResponse;
import subway.dto.LineStationsRequest;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineStationServiceTest {

    @Mock
    StationRepository stationRepository;
    @Mock
    LineRepository lineRepository;
    @Mock
    SectionRepository sectionRepository;
    @InjectMocks
    LineStationService lineStationService;

    @DisplayName("노선에 역을 추가한다")
    @Test
    void 노선에_역을_추가한다() {
        //given
        LineStationsRequest request = new LineStationsRequest(1L, 2L, 10);
        LineName name = new LineName("테스트노선");
        LineColor color = new LineColor("테스트색");
        Line line = new Line(1L, name, color, Sections.create());
        when(lineRepository.findById(1L)).thenReturn(line);
        Station station1 = new Station(1L, "테스트역1");
        Station station2 = new Station(2L, "테스트역2");
        when(stationRepository.findById(1L)).thenReturn(station1);
        when(stationRepository.findById(2L)).thenReturn(station2);
        //when
        lineStationService.saveLinesStations(line.getId(), request);
        //then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> verify(sectionRepository, times(1)).deleteAll(Collections.emptyList(), line),
                () -> verify(sectionRepository, times(1)).saveAll(List.of(new Section(station1, station2, new Distance(10))), line)
        );
    }

    @DisplayName("모든 노선의 역을 조회한다")
    @Test
    void 모든_노선의_역을_조회한다() {
        //given
        Station station1 = new Station(1L, "테스트역1");
        Station station2 = new Station(2L, "테스트역2");
        Station station3 = new Station(3L, "테스트역3");
        List<Line> lines = List.of(
                new Line(1L, new LineName("테스트노선1"), new LineColor("테스트색1"), new Sections(List.of(new Section(station1, station2, new Distance(10))))),
                new Line(2L, new LineName("테스트노선2"), new LineColor("테스트색2"), new Sections(List.of(new Section(station2, station3, new Distance(10)))))
        );
        when(lineRepository.findAll()).thenReturn(lines);
        //when
        List<LineStationResponse> allLinesStations = lineStationService.findAllLinesStations();
        //then
        Assertions.assertThat(allLinesStations).contains(
                new LineStationResponse(1L, "테스트노선1", "테스트색1", List.of(new StationResponse(1L, "테스트역1"), new StationResponse(2L, "테스트역2"))),
                new LineStationResponse(2L, "테스트노선2", "테스트색2", List.of(new StationResponse(2L, "테스트역2"), new StationResponse(3L, "테스트역3"))));
    }

    @DisplayName("노선의 역을 조회한다")
    @Test
    void 노선의_역을_조회한다() {
        //given
        Station station1 = new Station(1L, "테스트역1");
        Station station2 = new Station(2L, "테스트역2");
        Line line = new Line(1L, new LineName("테스트노선1"), new LineColor("테스트색1"), new Sections(List.of(new Section(station1, station2, new Distance(10)))));
        when(lineRepository.findById(1L)).thenReturn(line);
        //when
        LineStationResponse LineStation = lineStationService.findLinesStations(1L);
        //then
        Assertions.assertThat(LineStation).isEqualTo(new LineStationResponse(1L, "테스트노선1", "테스트색1", List.of(new StationResponse(1L, "테스트역1"), new StationResponse(2L, "테스트역2"))));
    }

    @DisplayName("노선의 역을 삭제한다")
    @Test
    void 노선의_역을_삭제한다() {
        //given
        Station station1 = new Station(1L, "테스트역1");
        Station station2 = new Station(2L, "테스트역2");
        Line line = new Line(1L, new LineName("테스트노선1"), new LineColor("테스트색1"), new Sections(List.of(new Section(station1, station2, new Distance(10)))));
        when(lineRepository.findById(1L)).thenReturn(line);
        when(stationRepository.findById(1L)).thenReturn(station1);
        //when
        lineStationService.deleteLinesStations(1L, 1L);
        //then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> verify(sectionRepository, times(1)).deleteAll(List.of(new Section(station1, station2, new Distance(10))), line),
                () -> verify(sectionRepository, times(1)).saveAll(Collections.emptyList(), line)
        );
    }
}
