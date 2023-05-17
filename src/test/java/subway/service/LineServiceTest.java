package subway.service;

import static java.lang.Long.MAX_VALUE;
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
import subway.dto.LineAddRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.exception.LineAlreadyExistsException;
import subway.exception.LineNotFoundException;
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
        final LineAddRequest request = new LineAddRequest("1호선", "RED", 0);

        // when
        final Long id = lineService.add(request);

        // then
        assertAll(
                () -> assertThat(lineRepository.findAll()).hasSize(1),
                () -> assertThat(id).isPositive()
        );
    }

    @Test
    void 라인을_저장할_때_이미_라인이_존재하는_경우_예외를_던진다() {
        // given
        final LineAddRequest request = new LineAddRequest("1호선", "RED", 0);
        lineService.add(request);

        // expect
        assertThatThrownBy(() -> lineService.add(request))
                .isInstanceOf(LineAlreadyExistsException.class)
                .hasMessage("노선이 이미 존재합니다.");
    }

    @Test
    void 라인id를_받아서_라인을_삭제한다() {
        // given
        final LineAddRequest request = new LineAddRequest("1호선", "RED", 0);
        final Long id = lineService.add(request);

        // when
        lineService.delete(id);

        // then
        assertThat(lineRepository.findAll()).isEmpty();
    }

    @Test
    void 라인id를_받아서_라인을_삭제할_때_라인이_존재하지_않는_경우_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> lineService.delete(MAX_VALUE))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 라인을_수정한다() {
        // given
        final Long id = lineService.add(new LineAddRequest("1호선", "RED", 0));
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
    void 라인id를_받아서_라인을_수정할_때_라인이_존재하지_않는_경우_예외를_던진다() {
        // given
        final LineUpdateRequest request = new LineUpdateRequest("2호선", "BLACK");

        // expect
        assertThatThrownBy(() -> lineService.update(MAX_VALUE, request))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 라인id로_라인을_조회한다() {
        // given
        final Long id = lineService.add(new LineAddRequest("1호선", "RED", 0));

        // when
        final LineResponse result = lineService.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("RED"),
                () -> assertThat(result.getSurcharge()).isEqualTo(0),
                () -> assertThat(result.getStations()).isEmpty()
        );
    }

    @Test
    void 라인id로_라인을_조회할_때_라인이_존재하지_않는_경우_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> lineService.findById(MAX_VALUE))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 라인을_전체_조회한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        )));
        lineRepository.save(new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 3),
                new Section("B", "Y", 2)
        )));

        // when
        final List<LineResponse> result = lineService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new LineResponse("1호선", "RED", 0, List.of("A", "B", "C", "D", "E")),
                new LineResponse("2호선", "BLUE", 0, List.of("Z", "B", "Y"))
        ));
    }
}
