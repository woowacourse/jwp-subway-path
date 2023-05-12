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
import subway.ui.dto.response.ReadLineResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineServiceTest {

    @InjectMocks
    LineService lineService;

    @Mock
    LineRepository lineRepository;

    @Mock
    SectionRepository sectionRepository;

    @Test
    void 노선을_저장하다() {
        // given
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        when(lineRepository.insert(any())).thenReturn(line);

        // when
        final Line actual = lineRepository.insert(line);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(line.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(line.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(line.getColor());
        });
    }

    @Test
    void 저장된_모든_노선을_가져온다() {
        // given
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final List<Line> lines = List.of(line);
        when(lineRepository.findAll()).thenReturn(lines);

        // when
        final List<Line> actual = lineRepository.findAll();

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
        final Line line = Line.of(1L, "2호선", "bg-yellow-500");
        final ReadLineResponse response = ReadLineResponse.of(line);
        when(lineRepository.findById(any())).thenReturn(line);
        doNothing().when(sectionRepository).findAllByLine(any());

        // when
        final ReadLineResponse actual = lineService.findLineById(1L);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(response.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(response.getName());
            softAssertions.assertThat(actual.getColor()).isEqualTo(response.getColor());
        });
    }
}
