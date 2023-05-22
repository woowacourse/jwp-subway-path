package subway.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LineService lineService;

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

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

    @Test
    @DisplayName("하나의 Line을 불러온다.")
    void load_line_by_id() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "#123456");
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        LineResponse expect = new LineResponse(lineResponse.getId(), lineResponse.getName(), lineResponse.getColor(), List.of());

        // when
        LineResponse result = lineService.readLine(lineResponse.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("전체 Line을 불러온다.")
    void load_line_all() {
        // given
        LineRequest line2Request = new LineRequest("2호선", "#123456");
        LineRequest line3Request = new LineRequest("3호선", "#abcdef");

        LineResponse line2Response = lineService.saveLine(line2Request);
        LineResponse line3Response = lineService.saveLine(line3Request);

        List<LineResponse> expect = List.of(line2Response, line3Response);

        // when
        List<LineResponse> result = lineService.readAllLine();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }
}
