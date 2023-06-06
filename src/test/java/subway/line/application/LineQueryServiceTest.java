package subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.application.exception.LineNotFoundException;
import subway.line.db.FakeLineRepository;
import subway.line.domain.interstation.InterStation;
import subway.line.domain.line.Line;
import subway.line.dto.response.LineResponseDto;

@DisplayNameGeneration(ReplaceUnderscores.class)
class LineQueryServiceTest {

    private FakeLineRepository fakeLineRepository;
    private LineQueryService lineQueryService;

    @BeforeEach
    void setUp() {
        fakeLineRepository = new FakeLineRepository();
        lineQueryService = new LineQueryService(fakeLineRepository);
    }

    @Test
    void 정상적으로_조회된다() {
        //given
        fakeLineRepository.save(new Line(1L, "2호선", "green", List.of(
                new InterStation(1L, 1L, 2L, 3),
                new InterStation(2L, 2L, 3L, 3)
        )));

        //when
        LineResponseDto result = lineQueryService.findById(1L);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    void 없는_id_가_조회되면_예외가_발생한다() {
        //given
        fakeLineRepository.save(new Line(1L, "2호선", "green", List.of(
                new InterStation(1L, 1L, 2L, 3),
                new InterStation(2L, 2L, 3L, 3)
        )));

        //when
        //then
        assertThatThrownBy(() -> lineQueryService.findById(2L))
                .isInstanceOf(LineNotFoundException.class);
    }

    @Test
    void 정상적으로_전체가_조회된다() {
        //given
        fakeLineRepository.save(new Line(1L, "2호선", "green", List.of(
                new InterStation(1L, 1L, 2L, 3),
                new InterStation(2L, 2L, 3L, 3)
        )));

        //when
        List<LineResponseDto> result = lineQueryService.findAllLines();

        //then
        assertThat(result).hasSize(1);
    }
}
