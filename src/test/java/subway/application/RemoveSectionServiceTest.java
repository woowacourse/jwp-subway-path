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
class RemoveSectionServiceTest {

    @InjectMocks
    RemoveSectionService removeSectionService;

    @Mock
    StationRepository stationRepository;

    @Mock
    LineRepository lineRepository;

    @Mock
    SectionRepository sectionRepository;

    @Test
    void 노선에_등록된_모든_역을_삭제한다() {
        when(lineRepository.findById(any())).thenReturn(Line.of(1L, "잠실역", "bg-blue-500"));
        doNothing().when(sectionRepository).findAllByLine(any());
        doNothing().when(sectionRepository).insert(any());

        assertDoesNotThrow(() -> removeSectionService.removeAllStation(1L));
    }

    @Test
    void 노선의_종점에_등록된_역을_삭제한다() {
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final Station stationOne = Station.of(1L, "잠실역");
        final Station stationTwo = Station.of(2L, "선릉역");
        final Station stationThree = Station.of(3L, "사당역");
        line.addInitialStations(stationOne, stationTwo, Distance.from(10));
        line.addEndStation(stationTwo, stationThree, Distance.from(10));

        when(lineRepository.findById(any())).thenReturn(line);
        when(stationRepository.findById(any())).thenReturn(stationThree);
        doNothing().when(sectionRepository).findAllByLine(any());
        doNothing().when(sectionRepository).insert(any());

        assertDoesNotThrow(() -> removeSectionService.removeEndStation(line.getId(), stationThree.getId()));
    }

    @Test
    void 노선의_중간에_등록된_역을_삭제한다() {
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final Station stationOne = Station.of(1L, "잠실역");
        final Station stationTwo = Station.of(2L, "선릉역");
        final Station stationThree = Station.of(3L, "사당역");
        line.addInitialStations(stationOne, stationTwo, Distance.from(10));
        line.addEndStation(stationTwo, stationThree, Distance.from(10));

        when(lineRepository.findById(any())).thenReturn(line);
        when(stationRepository.findById(any())).thenReturn(stationTwo);
        doNothing().when(sectionRepository).findAllByLine(any());
        doNothing().when(sectionRepository).insert(any());

        assertDoesNotThrow(() -> removeSectionService.removeMiddleStation(line.getId(), stationTwo.getId()));
    }
}
