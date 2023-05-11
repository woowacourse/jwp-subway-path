package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineUpdateRequest;
import subway.repository.LineRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 라인을_저장한다() {
        // given
        final LineSaveRequest request = new LineSaveRequest("1호선", "RED");

        // when
        final Long id = lineService.save(request);

        // then
        assertAll(
                () -> assertThat(lineRepository.findAll()).hasSize(1),
                () -> assertThat(id).isPositive()
        );
    }

    @Test
    void 라인id를_받아서_라인을_삭제한다() {
        // given
        final LineSaveRequest request = new LineSaveRequest("1호선", "RED");
        final Long id = lineService.save(request);

        // when
        lineService.delete(id);

        // then
        assertThat(lineRepository.findAll()).isEmpty();
    }

    @Test
    void 라인을_수정한다() {
        // given
        final Long id = lineService.save(new LineSaveRequest("1호선", "RED"));
        final LineUpdateRequest request = new LineUpdateRequest("2호선", "BLACK");

        // when
        lineService.update(id, request);

        // then
        final Line result = lineRepository.findAll().get(0);
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("BLACK")
        );
    }

    @Test
    void 라인id로_라인을_조회한다() {
        // given
        final Long id = lineService.save(new LineSaveRequest("1호선", "RED"));

        // when
        final LineResponse result = lineService.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("RED"),
                () -> assertThat(result.getStations()).isEmpty()
        );
    }

    @Test
    void 라인을_전체_조회한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        )));
        lineRepository.save(new Line("2호선", "BLUE", List.of(
                new Section("Z", "B", 3),
                new Section("B", "Y", 2)
        )));

        // when
        final List<LineResponse> result = lineService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new LineResponse("1호선", "RED", List.of("A", "B", "C", "D", "E")),
                new LineResponse("2호선", "BLUE", List.of("Z", "B", "Y"))
        ));
    }
}
