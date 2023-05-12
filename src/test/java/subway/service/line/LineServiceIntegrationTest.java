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
import subway.entity.LineEntity;
import subway.service.LineService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        LineCreateRequest req = new LineCreateRequest("잠실역", 2L, "green");

        // when
        lineService.saveLine(req);

        // then
        List<LineEntity> lineEntities = lineService.findAll();
        assertThat(lineEntities.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void find_all_lines_success() {
        // given
        LineCreateRequest green = new LineCreateRequest("2호선", 2L, "green");
        lineService.saveLine(green);

        LineCreateRequest red = new LineCreateRequest("8호선", 8L, "red");
        lineService.saveLine(red);

        // when
        List<LineEntity> result = lineService.findAll();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getLineNumber()).isEqualTo(green.getLineNumber()),
                () -> assertThat(result.get(1).getLineNumber()).isEqualTo(red.getLineNumber())
        );
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void delete_line_success() {
        // given
        LineCreateRequest green = new LineCreateRequest("2호선", 2L, "green");
        Long id = lineService.saveLine(green);

        // when
        lineService.deleteLineById(id);

        // then
        List<LineEntity> result = lineService.findAll();
        assertThat(result.size()).isEqualTo(0);
    }
}
