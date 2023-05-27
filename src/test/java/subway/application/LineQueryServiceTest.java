package subway.application;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static subway.fixtures.domain.LineFixture.SECOND_LINE;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineQueryServiceTest {

    @InjectMocks
    LineQueryService lineQueryService;

    @Mock
    LineRepository lineRepository;

    @Mock
    SectionRepository sectionRepository;

    @Test
    void 저장된_모든_노선을_가져온다() {
        // given
        final List<Line> lines = List.of(SECOND_LINE);
        when(lineRepository.findAll()).thenReturn(lines);
        when(sectionRepository.findLineInAllSectionByLineId(SECOND_LINE.getId())).thenReturn(SECOND_LINE);

        // when
        final List<Line> actualLines = lineQueryService.findAllLine();
        final Line actualLine = lineQueryService.findAllLine().get(0);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualLines).hasSize(1);
            softAssertions.assertThat(actualLine.getId()).isEqualTo(SECOND_LINE.getId());
            softAssertions.assertThat(actualLine.getName()).isEqualTo(SECOND_LINE.getName());
            softAssertions.assertThat(actualLine.getColor()).isEqualTo(SECOND_LINE.getColor());
        });
    }

    @Test
    void 아이디를_통해_저장된_노선을_가져온다() {
        // given
        when(sectionRepository.findLineInAllSectionByLineId(SECOND_LINE.getId())).thenReturn(SECOND_LINE);

        // when
        final Line actual = lineQueryService.findLineById(SECOND_LINE.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(SECOND_LINE.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(SECOND_LINE.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(SECOND_LINE.getColor());
        });
    }

}
