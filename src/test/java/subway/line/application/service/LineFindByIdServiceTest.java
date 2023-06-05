package subway.line.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.interstation.domain.InterStation;
import subway.line.application.port.in.LineNotFoundException;
import subway.line.application.port.in.LineResponseDto;
import subway.line.domain.Line;
import subway.line.out.FakeLineRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선 조회 기능 테스트")
class LineFindByIdServiceTest {

    private FakeLineRepository fakeLineRepository;
    private LineFindByIdService lineFindByIdService;

    @BeforeEach
    void setUp() {
        fakeLineRepository = new FakeLineRepository();
        lineFindByIdService = new LineFindByIdService(fakeLineRepository);
    }

    @Test
    void 정상적으로_조회된다() {
        //given
        fakeLineRepository.save(new Line(1L, "2호선", "green", List.of(
                new InterStation(1L, 1L, 2L, 3),
                new InterStation(2L, 2L, 3L, 3)
        )));

        //when
        LineResponseDto result = lineFindByIdService.findById(1L);

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
        assertThatThrownBy(() -> lineFindByIdService.findById(2L))
                .isInstanceOf(LineNotFoundException.class);
    }
}
