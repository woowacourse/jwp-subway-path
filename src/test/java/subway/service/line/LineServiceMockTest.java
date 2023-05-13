package subway.service.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dto.line.LineCreateRequest;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.service.LineService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.factory.LineEntityFactory.createLineEntity;
import static subway.factory.LineEntityFactory.createLineEntity2;

@ExtendWith(MockitoExtension.class)
class LineServiceMockTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

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
        List<LineEntity> expectedLines = List.of(createLineEntity(), createLineEntity2());
        when(lineService.findAll()).thenReturn(expectedLines);

        // when
        List<LineEntity> result = lineService.findAll();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getLineNumber()).isEqualTo(2),
                () -> assertThat(result.get(1).getLineNumber()).isEqualTo(8)
        );
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
    }
}
