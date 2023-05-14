package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import subway.exception.LineNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Nested
    class 성공_테스트 {
        @Test
        void 노선을_저장한다() {
            // given
            final Line line = new Line("2호선", "RED", List.of(
                    new Section("B", "C", 3),
                    new Section("A", "B", 2),
                    new Section("D", "E", 5),
                    new Section("C", "D", 4)
            ));

            // when
            final Line savedLine = lineRepository.save(line);

            // then
            assertThat(savedLine).usingRecursiveComparison().isEqualTo(line);
        }

        @Test
        void 노선을_조회한다() {
            // given
            final Line line = new Line("2호선", "RED", List.of(
                    new Section("B", "C", 3),
                    new Section("A", "B", 2),
                    new Section("D", "E", 5),
                    new Section("C", "D", 4)
            ));
            lineRepository.save(line);

            // when
            final List<Line> result = lineRepository.findAll();

            // then
            assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(line);
        }

        @Test
        void 노선을_삭제한다() {
            // given
            final Line line = new Line("2호선", "RED", List.of(
                    new Section("B", "C", 3),
                    new Section("A", "B", 2),
                    new Section("D", "E", 5),
                    new Section("C", "D", 4)
            ));
            final Line savedLine = lineRepository.save(line);
            final Long id = lineRepository.findIdByName(savedLine.getName());

            // when
            lineRepository.deleteById(id);

            // then
            assertThat(lineRepository.findAll()).isEmpty();
        }

        @Test
        void 이름을_입력받아_라인의_id를_반환한다() {
            // given
            final Line line = new Line("2호선", "RED", List.of(
                    new Section("B", "C", 3),
                    new Section("A", "B", 2),
                    new Section("D", "E", 5),
                    new Section("C", "D", 4)
            ));
            lineRepository.save(line);

            // when
            final Long id = lineRepository.findIdByName("2호선");

            // then
            assertThat(id).isPositive();
        }

        @Test
        void id를_입력받아_라인을_반환한다() {
            // given
            final Line line = new Line("2호선", "RED", List.of(
                    new Section("B", "C", 3),
                    new Section("A", "B", 2),
                    new Section("D", "E", 5),
                    new Section("C", "D", 4)
            ));
            lineRepository.save(line);
            final Long id = lineRepository.findIdByName(line.getName());

            // when
            final Line result = lineRepository.findById(id);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(line);
        }
    }

    @Nested
    class 예외_테스트 {
        @Test
        void 존재하지_않는_id를_입력받아_삭제할_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> lineRepository.deleteById(1L))
                    .isInstanceOf(LineNotFoundException.class)
                    .hasMessage("노선을 찾을 수 없습니다.");
        }

        @Test
        void 존재하지_않는_id를_입력받아_수정할_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> lineRepository.updateNameAndColorById(1L, "1호선", "RED"))
                    .isInstanceOf(LineNotFoundException.class)
                    .hasMessage("노선을 찾을 수 없습니다.");
        }

        @Test
        void 존재하지_않는_id를_입력받아_조회할_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> lineRepository.findById(1L))
                    .isInstanceOf(LineNotFoundException.class)
                    .hasMessage("노선을 찾을 수 없습니다.");
        }

        @Test
        void 존재하지_않는_노선_이름을_입력받아_조회할_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> lineRepository.findIdByName("1호선"))
                    .isInstanceOf(LineNotFoundException.class)
                    .hasMessage("노선을 찾을 수 없습니다.");
        }
    }
}
