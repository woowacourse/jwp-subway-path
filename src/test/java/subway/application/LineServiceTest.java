package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Fixture;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.SectionMap;
import subway.dto.SectionRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    @Test
    @DisplayName("노선에 첫 역 삽입 기능")
    void saveInitialSection() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        given(lineRepository.findLineById(1L)).willReturn(Fixture.line);
        given(stationService.findStationById(1L)).willReturn(Fixture.stationA);
        given(stationService.findStationById(2L)).willReturn(Fixture.stationB);
        given(lineRepository.saveSectionsByLine(Fixture.line)).willReturn(List.of(1L));

        // when & then
        assertDoesNotThrow(() -> lineService.saveInitialSection(1L, sectionRequest));
        verify(lineRepository, times(1)).saveSectionsByLine(any());
    }

    @Test
    @DisplayName("노선에 역 삽입 기능")
    void saveSection() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(2L, 3L, 10);
        final SectionMap sectionMap = SectionMap.generateBySections(List.of(Fixture.sectionAB), Fixture.stationA);
        final Line line = new Line(1L, "2호선", "green", sectionMap);

        given(lineRepository.findLineById(1L)).willReturn(line);
        given(stationService.findStationById(2L)).willReturn(Fixture.stationB);
        given(stationService.findStationById(3L)).willReturn(Fixture.stationC);
        given(lineRepository.saveSectionsByLine(line)).willReturn(List.of(1L, 2L));

        // when & then
        assertDoesNotThrow(() -> lineService.saveSection(1L, sectionRequest));
        verify(lineRepository, times(1)).saveSectionsByLine(any());
    }

    @Test
    @DisplayName("노선에 역 삭제 기능")
    void deleteStationById() {
        // given
        final SectionMap sectionMap = SectionMap.generateBySections(List.of(Fixture.sectionAB), Fixture.stationA);
        final Line line = new Line(1L, "2호선", "green", sectionMap);

        given(lineRepository.findLineById(1L)).willReturn(line);
        given(stationService.findStationById(2L)).willReturn(Fixture.stationB);
        given(lineRepository.saveSectionsByLine(line)).willReturn(List.of());

        // when & then
        assertDoesNotThrow(() -> lineService.deleteStationById(1L, 2L));
        verify(lineRepository, times(1)).saveSectionsByLine(any());
    }
}
