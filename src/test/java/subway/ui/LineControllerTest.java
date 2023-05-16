package subway.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

class LineControllerTest {
    
    @Test
    @DisplayName("호선 생성 테스트")
    void createLine() {
        // given
        final LineService lineServiceMock = mock(LineService.class);
        final LineRequest lineRequest = new LineRequest("2호선", "초록색");
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "초록색");
        final LineController lineController = new LineController(lineServiceMock);
        // when
        when(lineServiceMock.saveLine(lineRequest)).thenReturn(lineResponse);
        final ResponseEntity<LineResponse> lineResponseEntity = lineController.createLine(lineRequest);
        // then
        Assertions.assertThat(lineResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(lineResponseEntity.getBody()).isEqualTo(lineResponse);
    }
    
    @Test
    @DisplayName("모든 호선 조회 테스트")
    void findAllLines() {
        // given
        final LineService lineServiceMock = mock(LineService.class);
        final LineResponse lineResponse1 = new LineResponse(1L, "2호선", "초록색");
        final LineResponse lineResponse2 = new LineResponse(2L, "3호선", "오렌지색");
        final LineController lineController = new LineController(lineServiceMock);
        // when
        when(lineServiceMock.findLineResponses()).thenReturn(List.of(lineResponse1, lineResponse2));
        final ResponseEntity<List<LineResponse>> lineResponseEntity = lineController.findAllLines();
        // then
        Assertions.assertThat(lineResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(lineResponseEntity.getBody()).isEqualTo(List.of(lineResponse1, lineResponse2));
    }
    
    @Test
    @DisplayName("특정 호선 조회 테스트")
    void findLineById() {
        // given
        final LineService lineServiceMock = mock(LineService.class);
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "초록색");
        final LineController lineController = new LineController(lineServiceMock);
        // when
        when(lineServiceMock.findLineResponseById(1L)).thenReturn(lineResponse);
        final ResponseEntity<LineResponse> lineResponseEntity = lineController.findLineById(1L);
        // then
        Assertions.assertThat(lineResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(lineResponseEntity.getBody()).isEqualTo(lineResponse);
    }
    
    @Test
    @DisplayName("호선 수정 테스트")
    void updateLine() {
        // given
        final LineService lineServiceMock = mock(LineService.class);
        final LineRequest lineRequest = new LineRequest("2호선", "초록색");
        final LineController lineController = new LineController(lineServiceMock);
        // when
        final ResponseEntity<Void> lineResponseEntity = lineController.updateLine(1L, lineRequest);
        // then
        Assertions.assertThat(lineResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @DisplayName("호선 삭제 테스트")
    void deleteLine() {
        // given
        final LineService lineServiceMock = mock(LineService.class);
        final LineController lineController = new LineController(lineServiceMock);
        // when
        final ResponseEntity<Void> lineResponseEntity = lineController.deleteLine(1L);
        // then
        Assertions.assertThat(lineResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}