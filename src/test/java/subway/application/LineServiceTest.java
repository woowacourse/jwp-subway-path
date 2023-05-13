package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    @Mock
    private LineDao lineDao;

    @InjectMocks
    private LineService lineService;

    @DisplayName("새로운 노선을 추가한다.")
    @Test
    void createLine() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "green");
        Line line = new Line(1L, "2호선", "green");
        LineResponse lineResponse = LineResponse.of(line);
        when(lineDao.insert(any(Line.class)))
                .thenReturn(line);

        // when
        LineResponse result = lineService.saveLine(lineRequest);

        // then
        assertThat(result.getId()).isEqualTo(lineResponse.getId());
        assertThat(result.getName()).isEqualTo(lineResponse.getName());
        assertThat(result.getColor()).isEqualTo(lineResponse.getColor());
    }

    @DisplayName("이미 등록된 노선을 추가하면 예외를 던진다.")
    @Test
    void createDuplicatedLine() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "green");
        Line line = new Line(1L, "2호선", "green");
        when(lineDao.findByName(any()))
                .thenReturn(Optional.of(line));

        // when, then
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("등록되지 않은 노선을 변경하려 할 때 예외를 던진다.")
    @Test
    void updateNotExistingLine() {
        // given
        Long lineId = 1L;
        LineRequest lineRequest = new LineRequest("2호선", "green");
        when(lineDao.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> lineService.updateLine(lineId, lineRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("등록되지 않은 노선을 삭제하려 할 때 예외를 던진다.")
    @Test
    void deleteNotExistingLine() {
        // given
        Long lineId = 1L;
        when(lineDao.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> lineService.deleteLineById(lineId))
                .isInstanceOf(NotFoundException.class);
    }
}
