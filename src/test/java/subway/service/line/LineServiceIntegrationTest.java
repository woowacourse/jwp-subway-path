package subway.service.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineEditRequest;
import subway.dto.line.LineResponse;
import subway.dto.line.LinesResponse;
import subway.exception.LineNotFoundException;
import subway.service.LineService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class LineServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LineService lineService;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("노선을 저장한다.")
    void save_line_success() {
        // given
        LineCreateRequest req = new LineCreateRequest("잠실역", 2, "green");

        // when
        lineService.saveLine(req);

        // then
        LinesResponse lineEntities = lineService.findAll();
        assertThat(lineEntities.getLines().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void find_all_lines_success() {
        // given
        LineCreateRequest green = new LineCreateRequest("2호선", 2, "green");
        lineService.saveLine(green);

        LineCreateRequest red = new LineCreateRequest("8호선", 8, "red");
        lineService.saveLine(red);

        // when
        LinesResponse result = lineService.findAll();

        // then
        assertAll(
                () -> assertThat(result.getLines().size()).isEqualTo(2),
                () -> assertThat(result.getLines().get(0).getLineNumber()).isEqualTo(green.getLineNumber()),
                () -> assertThat(result.getLines().get(1).getLineNumber()).isEqualTo(red.getLineNumber())
        );
    }

    @Test
    @DisplayName("노선을 수정한다.")
    void edit_line_success() {
        // given
        LineCreateRequest line = new LineCreateRequest("2호선", 2, "green");
        Long id = lineService.saveLine(line);

        LineEditRequest lineEditRequest = new LineEditRequest("2호선", 2, "blue");

        // when
        lineService.editLineById(id, lineEditRequest);

        // then
        LineResponse lineEntity = lineService.findAll().getLines().get(0);
        assertThat(lineEntity.getColor()).isEqualTo(lineEditRequest.getColor());
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
        LineCreateRequest green = new LineCreateRequest("2호선", 2, "green");
        Long id = lineService.saveLine(green);

        // when
        lineService.deleteLineById(id);

        // then
        LinesResponse result = lineService.findAll();
        assertThat(result.getLines().size()).isEqualTo(0);
    }
}
