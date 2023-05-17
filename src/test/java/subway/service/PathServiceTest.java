package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.dto.LineDto;
import subway.dto.SectionDto;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.repository.LineRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 최단_경로를_탐색한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2),
                new Section("B", "C", 100)
        )));
        lineRepository.save(new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 2),
                new Section("Y", "Z", 5),
                new Section("C", "Y", 3)
        )));

        final ShortestPathRequest request = new ShortestPathRequest("C", "A", 15);

        // when
        final ShortestPathResponse result = pathService.shortestPath(request);

        // then
        assertAll(
                () -> assertThat(result.getFare()).isEqualTo(800),
                () -> assertThat(result.getDistance()).isEqualTo(12),
                () -> assertThat(result.getPath())
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(
                                new LineDto(
                                        "2호선", "BLUE", List.of(
                                        new SectionDto("C", "Y", 3),
                                        new SectionDto("Y", "Z", 5),
                                        new SectionDto("Z", "B", 2)
                                )),
                                new LineDto(
                                        "1호선", "RED", List.of(
                                        new SectionDto("A", "B", 2)
                                ))
                        ))
        );
    }

    @Test
    void 올바르지_않은_출발역과_도착역을_입력하면_예외를_던진다() {
        // given
        final ShortestPathRequest request = new ShortestPathRequest("C", "A", 15);

        // expect
        assertThatThrownBy(() -> pathService.shortestPath(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
