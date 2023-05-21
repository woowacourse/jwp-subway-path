package subway.service.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineEditRequest;
import subway.dto.line.LinesResponse;
import subway.entity.LineEntity;
import subway.event.RouteUpdateEvent;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;
import subway.service.LineService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.fixture.LineEntityFixture.createLineEntity;
import static subway.fixture.LineEntityFixture.createLineEntity2;

@ExtendWith(MockitoExtension.class)
class LineServiceUnitTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    @DisplayName("노선을 저장한다.")
    void save_line_success() {
        // given
        LineCreateRequest req = new LineCreateRequest("잠실역", 2, "green");

        // when
        lineService.saveLine(req);

        // then
        verify(lineRepository).insertLine(any(LineEntity.class));
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void find_all_lines_success() {
        // given
        List<LineEntity> lineEntities = List.of(createLineEntity(), createLineEntity2());
        when(lineRepository.findAll()).thenReturn(lineEntities);

        // when
        LinesResponse result = lineService.findAll();

        // then
        assertAll(
                () -> assertThat(result.getLines().size()).isEqualTo(2),
                () -> assertThat(result.getLines().get(0).getLineNumber()).isEqualTo(2),
                () -> assertThat(result.getLines().get(1).getLineNumber()).isEqualTo(8)
        );
    }

    @Test
    @DisplayName("노선을 수정한다.")
    void edit_line_success() {
        // given
        Long id = 1L;
        LineEditRequest lineEditRequest = new LineEditRequest("2호선", 2, "blue");

        LineEntity lineEntity = createLineEntity();
        given(lineRepository.findById(id)).willReturn(Optional.of(lineEntity));

        // when
        lineService.editLineById(id, lineEditRequest);

        // then
        assertThat(lineEntity.getColor()).isEqualTo(lineEditRequest.getColor());
        verify(publisher).publishEvent(any(RouteUpdateEvent.class));
    }

    @Test
    @DisplayName("노선을 못 찾는다면 예외를 발생시킨다.")
    void throws_exception_when_line_not_found() {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> lineService.editLineById(id, any()))
                .isInstanceOf(LineNotFoundException.class);
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void delete_line_success() {
        // given
        Long id = 1L;

        // when
        lineService.deleteLineById(id);

        // then
        verify(lineRepository).deleteLineById(id);
        verify(publisher).publishEvent(any(RouteUpdateEvent.class));
    }
}
