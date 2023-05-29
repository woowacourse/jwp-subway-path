package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.domain.LineColor;
import subway.domain.LineName;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    LineRepository lineRepository;
    @Mock
    SectionRepository sectionRepository;
    @InjectMocks
    LineService lineService;

    @DisplayName("노선을 저장한다")
    @Test
    void 노선을_저장한다() {
        //given
        LineRequest request = new LineRequest("new호선", "빨강");
        Line line = new Line(new LineName("new호선"), new LineColor("빨강"), Sections.create());
        when(lineRepository.save(line)).thenReturn(new Line(3L, new LineName("new호선"), new LineColor("빨강"), Sections.create()));
        //when
        Long id = lineService.saveLine(request);
        //then
        assertThat(id).isEqualTo(3L);
    }

    @DisplayName("노선을 조회한다")
    @Test
    void 노선을_조회한다() {
        //given
        Line line = new Line(1L, new LineName("2호선"), new LineColor("초록"), Sections.create());
        when(lineRepository.findById(1L)).thenReturn(line);
        LineResponse expected = new LineResponse(1L, "2호선", "초록");
        //when
        LineResponse response = lineService.findLine(1L);
        //then
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("노선을 수정한다")
    @Test
    void 노선을_수정한다() {
        //given
        LineRequest request = new LineRequest("수정", "빨강");
        Line line = new Line(1L, new LineName("2호선"), new LineColor("초록"), Sections.create());
        when(lineRepository.findById(1L)).thenReturn(line);
        Line updateLine = new Line(1L, new LineName("수정"), new LineColor("빨강"), Sections.create());
        //when
        lineService.editLine(1L, request);
        //then
        verify(lineRepository, times(1)).update(updateLine);
    }

    @DisplayName("노선을_삭제한다")
    @Test
    void 노선을_삭제한다() {
        //given
        Line line = new Line(1L, new LineName("2호선"), new LineColor("초록"), Sections.create());
        when(lineRepository.findById(1L)).thenReturn(line);
        //when
        lineService.deleteLine(1L);
        //then
        assertAll(
                () -> verify(sectionRepository, times(1)).deleteAll(line.getSections(), line),
                () -> verify(lineRepository, times(1)).delete(line)
        );
    }
}
