package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

@SpringBootTest
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("Line을 저장한다.")
    void save_line() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "#123456");

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        assertThat(lineResponse.getName()).isEqualTo(lineRequest.getName());
        assertThat(lineResponse.getColor()).isEqualTo(lineRequest.getColor());
    }

}