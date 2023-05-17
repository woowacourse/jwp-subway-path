package subway.repository;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.line.Line;
import subway.integration.IntegrationTest;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.fixture.EntityFixture.일호선_남색_Entity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineRepositoryTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 호선을_저장한다() {
        //when
        final Line line = lineRepository.save(일호선_남색_Entity);

        //then
        assertSoftly(softly -> {
            softly.assertThat(line.getId()).isEqualTo(1L);
            softly.assertThat(line.getNameValue()).isEqualTo("일호선");
            softly.assertThat(line.getColorValue()).isEqualTo("남색");
            softly.assertThat(line.getSections()).isEmpty();
        });
    }

    @Test
    void 전체_호선을_조회한다() {
        //given
        lineRepository.save(일호선_남색_Entity);

        //when
        final List<Line> lines = lineRepository.findLines();

        //then
        assertSoftly(softly -> {
            softly.assertThat(lines).hasSize(1);

            final Line line = lines.get(0);
            softly.assertThat(line.getId()).isEqualTo(1L);
            softly.assertThat(line.getNameValue()).isEqualTo("일호선");
            softly.assertThat(line.getColorValue()).isEqualTo("남색");
            softly.assertThat(line.getSections()).isEmpty();
        });
    }

    @Test
    void id로_호선을_조회한다() {
        //given
        final Long id = lineRepository.save(일호선_남색_Entity).getId();

        //when
        final Line line = lineRepository.findLineById(id);

        //then
        assertSoftly(softly -> {
            softly.assertThat(line.getId()).isEqualTo(1L);
            softly.assertThat(line.getNameValue()).isEqualTo("일호선");
            softly.assertThat(line.getColorValue()).isEqualTo("남색");
            softly.assertThat(line.getSections()).isEmpty();
        });
    }
}
