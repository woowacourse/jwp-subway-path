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
import subway.domain.section.Section;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static subway.fixtures.domain.LineFixture.SECOND_LINE;
import static subway.fixtures.domain.StationFixture.JAMSIL;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG;

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
        when(lineRepository.insert(any())).thenReturn(SECOND_LINE);

        // when
        final Line actual = lineCommandService.saveLine(SECOND_LINE.getName(), SECOND_LINE.getColor());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(SECOND_LINE.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(SECOND_LINE.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(SECOND_LINE.getColor());
        });
    }

    @Test
    void 아이디를_통해_저장된_노선을_가져온다() {
        // given
        when(sectionRepository.findLineInAllSectionByLineId(SECOND_LINE.getId())).thenReturn(SECOND_LINE);

        // when
        final Line actual = lineCommandService.findLineById(SECOND_LINE.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(SECOND_LINE.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(SECOND_LINE.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(SECOND_LINE.getColor());
        });
    }

    @Test
    void 아이디를_통해_노선을_삭제하다() {
        doNothing().when(lineRepository).deleteById(any());

        assertDoesNotThrow(() -> lineCommandService.deleteLineById(SECOND_LINE.getId()));
    }

    @Test
    void 구역을_저장하다() {
        // given
        when(stationRepository.findById(JAMSIL.getId())).thenReturn(JAMSIL);
        when(stationRepository.findById(SEOLLEUNG.getId())).thenReturn(SEOLLEUNG);
        when(sectionRepository.findLineInAllSectionByLineId(SECOND_LINE.getId())).thenReturn(SECOND_LINE);
        doNothing().when(sectionRepository).insert(any());

        // when, then
        assertDoesNotThrow(() -> lineCommandService.saveSection(
                SECOND_LINE.getId(),
                JAMSIL.getId(),
                SEOLLEUNG.getId(),
                10));
    }

    @Test
    void 구역을_삭제하다() {
        // given
        final Section section = Section.of(JAMSIL, SEOLLEUNG, Distance.from(10));
        SECOND_LINE.addSection(section);

        when(sectionRepository.findLineInAllSectionByLineId(SECOND_LINE.getId())).thenReturn(SECOND_LINE);
        when(stationRepository.findById(JAMSIL.getId())).thenReturn(JAMSIL);

        // when, then
        assertDoesNotThrow(() -> lineCommandService.deleteStation(SECOND_LINE.getId(), JAMSIL.getId()));
    }
}
