package subway.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.LineDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

@JdbcTest
@Import({LineService.class, LineDao.class})
class LineServiceTest {
    
    @Autowired
    private LineService lineService;
    
    @Test
    @DisplayName("라인 저장 테스트")
    void saveLineTest() {
        // given
        final int initialSize = this.lineService.findLineResponses().size();
        final LineRequest lineRequest = new LineRequest("테스트라인", "TEST");
        // when
        final LineResponse lineResponse = this.lineService.saveLine(lineRequest);
        // then
        final int finalSize = this.lineService.findLineResponses().size();
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getName()).isEqualTo(lineRequest.getName());
        assertThat(finalSize).isEqualTo(initialSize + 1);
    }
    
    @Test
    @DisplayName("라인 수정 테스트")
    void updateLineTest() {
        // given
        final LineRequest lineRequest = new LineRequest("테스트라인", "TEST");
        final LineResponse lineResponse = this.lineService.saveLine(lineRequest);
        // when
        this.lineService.updateLine(lineResponse.getId(), new LineRequest("수정라인", "TEST"));
        // then
        final LineResponse updatedLineResponse = this.lineService.findLineResponseById(
                lineResponse.getId());
        assertThat(updatedLineResponse).isNotNull();
        assertThat(updatedLineResponse.getName()).isEqualTo("수정라인");
    }
    
    @Test
    @DisplayName("라인 삭제 테스트")
    void deleteLineTest() {
        // given
        final LineRequest lineRequest = new LineRequest("테스트라인", "TEST");
        final LineResponse lineResponse = this.lineService.saveLine(lineRequest);
        final int initialSize = this.lineService.findLineResponses().size();
        // when
        this.lineService.deleteLineById(lineResponse.getId());
        // then
        final int finalSize = this.lineService.findLineResponses().size();
        assertThat(finalSize).isEqualTo(initialSize - 1);
    }
}