package subway.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AddSectionServiceTest {

    @InjectMocks
    AddSectionService addSectionService;

    @Mock
    StationRepository stationRepository;

    @Mock
    LineRepository lineRepository;

    @Mock
    SectionRepository sectionRepository;

    @Test
    void 노선에_처음_역을_저장할_때_2개_의_역을_추가한다() {
        when(lineRepository.findById(any())).thenReturn(Line.of(1L, "2호선", "bg-yellow-500"));
        when(stationRepository.findById(1L)).thenReturn(Station.of(1L, "잠실역"));
        when(stationRepository.findById(2L)).thenReturn(Station.of(2L, "선릉역"));
        doNothing().when(sectionRepository).findAllByLine(any());
        doNothing().when(sectionRepository).insert(any());

        assertDoesNotThrow(() -> addSectionService.addInitialStations(1L, 1L, 2L, 10));
    }

    @Test
    void 노선_종점에_역을_추가한다() {
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final Station stationOne = Station.of(1L, "잠실역");
        final Station stationTwo = Station.of(2L, "선릉역");
        final Station stationThree = Station.of(3L, "사당역");
        line.addInitialStations(stationOne, stationTwo, Distance.from(10));

        when(lineRepository.findById(any())).thenReturn(line);
        when(stationRepository.findById(2L)).thenReturn(stationTwo);
        when(stationRepository.findById(3L)).thenReturn(stationThree);
        doNothing().when(sectionRepository).findAllByLine(any());
        doNothing().when(sectionRepository).insert(any());

        assertDoesNotThrow(() -> addSectionService.addEndStation(1L, 2L, 3L, 10));
    }

    @Test
    void 노선_중간에_역을_추가한다() {
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final Station stationOne = Station.of(1L, "잠실역");
        final Station stationTwo = Station.of(2L, "선릉역");
        final Station stationThree = Station.of(3L, "사당역");
        line.addInitialStations(stationOne, stationTwo, Distance.from(10));

        when(lineRepository.findById(any())).thenReturn(line);
        when(stationRepository.findById(1L)).thenReturn(stationOne);
        when(stationRepository.findById(2L)).thenReturn(stationTwo);
        when(stationRepository.findById(3L)).thenReturn(stationThree);
        doNothing().when(sectionRepository).findAllByLine(any());
        doNothing().when(sectionRepository).insert(any());

        assertDoesNotThrow(() -> addSectionService.addMiddleStation(1L, 1L, 2L, 3L, 5));
    }
}
