package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.exception.StationNotFoundException;
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

    @Nested
    class 성공_테스트 {
        @Test
        void 최단_경로를_조회한다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));
            final ShortestPathRequest request = new ShortestPathRequest("A", "D");

            // when
            final ShortestPathResponse result = pathService.findShortestPath(request);

            // then
            assertAll(
                    () -> assertThat(result.getPath()).containsExactly("A", "B", "D"),
                    () -> assertThat(result.getDistance()).isEqualTo(4),
                    () -> assertThat(result.getFare()).isEqualTo(1250)
            );
        }
    }

    @Nested
    class 예외_테스트 {

        @Test
        void 입력된_출발역이_없는_역인_경우_예외를_던진다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));
            final ShortestPathRequest request = new ShortestPathRequest("Q", "D");

            // expect
            assertThatThrownBy(() -> pathService.findShortestPath(request))
                    .isInstanceOf(StationNotFoundException.class)
                    .hasMessageContaining("역을 찾을 수 없습니다.");
        }

        @Test
        void 입력된_도착역이_없는_역인_경우_예외를_던진다() {
            // given
            lineRepository.save(new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ));
            lineRepository.save(new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            ));
            final ShortestPathRequest request = new ShortestPathRequest("A", "Q");

            // expect
            assertThatThrownBy(() -> pathService.findShortestPath(request))
                    .isInstanceOf(StationNotFoundException.class)
                    .hasMessageContaining("역을 찾을 수 없습니다.");
        }
    }
}
