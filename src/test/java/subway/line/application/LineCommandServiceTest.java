package subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.interstation.domain.InterStation;
import subway.interstation.domain.InterStations;
import subway.line.application.dto.request.LineCreateRequestDto;
import subway.line.application.dto.request.LineUpdateRequestDto;
import subway.line.application.dto.response.InterStationResponseDto;
import subway.line.application.dto.response.LineResponseDto;
import subway.line.db.FakeLineRepository;
import subway.line.domain.Line;

@DisplayNameGeneration(ReplaceUnderscores.class)
class LineCommandServiceTest {

    private FakeLineRepository fakeLineRepository;
    private LineCommandService lineCommandService;

    @BeforeEach
    void setUp() {
        fakeLineRepository = new FakeLineRepository();
        lineCommandService = new LineCommandService(fakeLineRepository);
    }

    @Test
    void 정상적으로_생성된다() {
        LineResponseDto result = lineCommandService.createLine(new LineCreateRequestDto("2호선", "초록색", 1L, 2L, 10));
        LineResponseDto expected = new LineResponseDto(1L, "2호선", "초록색", List.of(
                new InterStationResponseDto(1L, 1L, 2L, 10)
        ));

        assertSoftly(
                softly -> {
                    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
                    assertThat(fakeLineRepository.findAll()).hasSize(1);
                }
        );
    }

    @Test
    void 정상적으로_지워진다() {
        // given
        Line savedLine = fakeLineRepository.save(new Line(1L, "2호선", "green", new InterStations(
                List.of(
                        new InterStation(1L, 1L, 2L, 3),
                        new InterStation(2L, 2L, 3L, 3)
                )
        )));

        // when
        lineCommandService.deleteLineById(savedLine.getId());

        // then
        assertThat(fakeLineRepository.findAll()).isEmpty();
    }

    @Test
    void 정상적으로_수정된다() {
        //given
        Line savedLine = fakeLineRepository.save(new Line(1L, "2호선", "green", List.of(
                new InterStation(1L, 1L, 2L, 3),
                new InterStation(2L, 2L, 3L, 3)
        )));

        //when
        lineCommandService.updateLine(new LineUpdateRequestDto(savedLine.getId(), "3호선", "orange"));

        //then
        Line result = fakeLineRepository.findById(savedLine.getId()).get();
        assertSoftly(
                softly -> {
                    softly.assertThat(result.getName().getValue()).isEqualTo("3호선");
                    softly.assertThat(result.getColor().getValue()).isEqualTo("orange");
                }
        );
    }
}
