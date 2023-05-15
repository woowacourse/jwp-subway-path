package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LineService.class)
class LineServiceTest {

    @MockBean
    private LineRepository lineRepository;
    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("요청의 이름, 색깔에 해당하는 라인을 생성한다.")
    void createLine() {
        // given
        LineRequest request = new LineRequest("2호선", "green");
        Line expectedLine = new Line(1L, request.getName(), request.getColor());
        when(lineRepository.save(any())).thenReturn(expectedLine);

        // when
        LineResponse response = lineService.createLine(request);

        // then
        assertThat(response.getId()).isEqualTo(expectedLine.getId());

        verify(lineRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("라인을 저장한다")
    void save() {
        // given
        Line line = new Line(1L, "잠실역", "green");

        // when
        lineService.save(line);

        // then
        verify(lineRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("id에 해당하는 라인을 요청의 이름과 색깔로 변경한다")
    void updateLine() {
        // given
        LineRequest request = new LineRequest("선릉역", "green");
        Line found = new Line(1L, "잠실역", "green");
        when(lineRepository.findById(found.getId())).thenReturn(Optional.of(found));

        // when
        lineService.updateLine(found.getId(), request);

        // then
        Line updated = new Line(found.getId(), request.getName(), request.getColor());
        verify(lineRepository, times(1)).save(refEq(updated, "sections"));
    }

    @Test
    @DisplayName("id에 해당하는 라인을 삭제한다")
    void deleteLineById() {
        // given
        Line line = new Line(1L, "잠실역", "green");
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        // when
        lineService.deleteLineById(1L);

        // then
        verify(lineRepository, times(1)).delete(line);
    }

    @Test
    @DisplayName("모든 라인을 조회한다")
    void findLineResponses() {
        // when
        lineService.findLineResponses();

        // then
        verify(lineRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("id에 해당하는 라인을 조회한다")
    void findLineResponseById() {
        // given
        Line line = new Line(1L, "잠실역", "green");
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        // when
        lineService.findLineResponseById(1L);

        verify(lineRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("id에 해당하는 라인을 조회한다")
    void findById() {
        // given
        Line line = new Line(1L, "잠실역", "green");
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        // when
        lineService.findById(1L);

        verify(lineRepository, times(1)).findById(1L);
    }
}