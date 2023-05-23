package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.repository.LineRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository.save(new Line("1호선", List.of(
                new Section("A", "B", 2),
                new Section("B", "C", 2)
        )));
        lineRepository.save(new Line("2호선", List.of(
                new Section("X", "B", 2),
                new Section("B", "C", 3)
        )));
        lineRepository.save(new Line("2호선", List.of(
                new Section("A", "Y", 50),
                new Section("Y", "C", 3)
        )));
    }

    @Test
    void 최단_거리와_요금을_조회한다() {
        // given
        PathRequest request = new PathRequest("A", "C");

        // when
        PathResponse path = pathService.findShortestPath(request);

        // then
        assertThat(path).usingRecursiveComparison()
                .isEqualTo(new PathResponse(
                        List.of(
                                new StationResponse("A"),
                                new StationResponse("B"),
                                new StationResponse("C")
                        ),
                        4,
                        1250
                ));
    }

    @Test
    void 출발지와_도착지가_같으면_예외가_발생한다() {
        // given
        PathRequest request = new PathRequest("A", "A");

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
