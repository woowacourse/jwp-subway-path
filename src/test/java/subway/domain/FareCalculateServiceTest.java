package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.fare.service.FareCalculateService;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.path.Path;
import subway.jgrapht.JgraphtSectionEdge;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class FareCalculateServiceTest {

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private FareCalculateService fareCalculateService;

    @ParameterizedTest(name = "이용거리가 {0}km 이고, 최대 노선 추가 운임이 {1}원이면서 {2}세일 때, 최종 운임 요금은 {3}원이다")
    @CsvSource(value = {
            "9, 0, 19, 1250",
            "12, 900, 19, 2250",
            "50, 10000, 5, 0",
            "58, 200, 12, 1000",
            "58, 200, 18, 1600",
    })
    void 운임_정책을_적용한_최종_운임_요금을_계산한다(final Long distance,
                                   final int maxAdditionalFare,
                                   final int age,
                                   final int expectedFare) {
        // given
        final Path mockPath = mock(Path.class);
        given(mockPath.getDistance())
                .willReturn(distance);
        given(mockPath.getSectionEdges())
                .willReturn(List.of(
                        new JgraphtSectionEdge(null, 1L),
                        new JgraphtSectionEdge(null, 2L))
                );
        given(lineRepository.findById(1L))
                .willReturn(new Line(1L, "1호선", "color1", 0));
        given(lineRepository.findById(2L))
                .willReturn(new Line(2L, "2호선", "color2", maxAdditionalFare));

        // when
        final int actual = fareCalculateService.calculate(mockPath, age);

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }
}
