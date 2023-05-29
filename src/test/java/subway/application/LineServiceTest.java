package subway.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import subway.application.repository.LineRepository;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Sql("/testSchema.sql")
@SpringBootTest
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @MockBean
    private LineRepository lineRepository;

    @Test
    void 호선_정보를_저장한다() {
        given(lineRepository.saveLine(any()))
                .willReturn(new Line(1L, "1호선", "blue"));
        final LineResponse lineResponse = lineService.saveLine(new LineRequest("1호선", "blue"));
        assertThat(lineResponse).usingRecursiveComparison().isEqualTo(new LineResponse(1L, "1호선", "blue"));
    }
}