package subway.application;

import org.assertj.core.api.SoftAssertions;
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
import subway.domain.section.Section;
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
class LineCommandServiceTest {

    @InjectMocks
    LineCommandService lineCommandService;

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    SectionRepository sectionRepository;

    @Test
    void 노선을_저장하다() {
        // given
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        when(lineRepository.insert(any())).thenReturn(line);

        // when
        final Line actual = lineCommandService.saveLine("2호선", "bg-yellow-500");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(line.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(line.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(line.getColor());
        });
    }

    @Test
    void 아이디를_통해_저장된_노선을_가져온다() {
        // given
        final Line line1 = Line.of(1L, "2호선", "bg-yellow-500");
        final Line line2 = Line.of(1L, "2호선", "bg-yellow-500");
        when(lineRepository.findById(any())).thenReturn(line1);
        when(sectionRepository.findAllSectionByLine(any())).thenReturn(line2);

        // when
        final Line actual = lineCommandService.findLineById(1L);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(line1.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(line1.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(line1.getColor());
        });
    }

    @Test
    void 아이디를_통해_노선을_삭제하다() {
        doNothing().when(lineRepository).deleteById(any());

        assertDoesNotThrow(() -> lineCommandService.deleteLineById(1L));
    }

    @Test
    void 구역을_저장하다() {
        // given
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final Station upStation = Station.of(1L, "잠실역");
        final Station downStation = Station.of(2L, "선릉역");
        final Line newLine = Line.of(1L, "2호선", "bg-yellow-500");

        when(lineRepository.findById(1L)).thenReturn(line);
        when(stationRepository.findById(1L)).thenReturn(upStation);
        when(stationRepository.findById(2L)).thenReturn(downStation);
        when(sectionRepository.findAllSectionByLine(any())).thenReturn(newLine);
        doNothing().when(sectionRepository).insert(any());

        // when, then
        assertDoesNotThrow(() -> lineCommandService.saveSection(1L, 1L, 2L, 10));
    }

    @Test
    void 구역을_삭제하다() {
        // given
        final Station upStation = Station.of(1L, "잠실역");
        final Station downStation = Station.of(2L, "선릉역");
        final Section section = Section.of(upStation, downStation, Distance.from(10));
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final Line newLine = Line.of(1L, "2호선", "bg-yellow-500");
        newLine.addSection(section);

        when(lineRepository.findById(1L)).thenReturn(line);
        when(sectionRepository.findAllSectionByLine(line)).thenReturn(newLine);
        when(stationRepository.findById(1L)).thenReturn(upStation);

        // when, then
        assertDoesNotThrow(() -> lineCommandService.deleteStation(1L, 1L));
    }
}
