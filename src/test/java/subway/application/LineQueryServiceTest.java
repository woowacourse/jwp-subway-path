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
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final List<Line> lines = List.of(line);
        when(lineRepository.findAll()).thenReturn(lines);
        when(sectionRepository.findAllSectionByLine(any())).thenReturn(line);

        // when
        final List<Line> actual = lineQueryService.findAllLine();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(line.getId());
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo(line.getName());
            softAssertions.assertThat(actual.get(0).getColor()).isEqualTo(line.getColor());
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
        final Line actual = lineQueryService.findLineById(1L);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(line1.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(line1.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(line1.getColor());
        });
    }

}
