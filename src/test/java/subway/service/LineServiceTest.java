package subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.service.line.LineService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Test
    void 노선_정보를_저장한다() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");

        lineService.createLine(lineCreateRequest);

        final List<LineResponse> lineResponses = lineService.findAll();
        assertThat(lineResponses.size()).isEqualTo(1);
    }

    @Test
    void 모든_노선_정보를_조회한다() {
        final LineCreateRequest lineCreateRequest1 = new LineCreateRequest("2호선", 2L, "초록색");
        final LineCreateRequest lineCreateRequest2 = new LineCreateRequest("4호선", 4L, "하늘색");
        lineService.createLine(lineCreateRequest1);
        lineService.createLine(lineCreateRequest2);

        final List<LineResponse> lineResponses = lineService.findAll();

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(lineResponses.get(0).getName()).isEqualTo("2호선");
        softAssertions.assertThat(lineResponses.get(0).getLineNumber()).isEqualTo(2L);
        softAssertions.assertThat(lineResponses.get(0).getColor()).isEqualTo("초록색");

        softAssertions.assertThat(lineResponses.get(1).getName()).isEqualTo("4호선");
        softAssertions.assertThat(lineResponses.get(1).getLineNumber()).isEqualTo(4L);
        softAssertions.assertThat(lineResponses.get(1).getColor()).isEqualTo("하늘색");
        softAssertions.assertAll();
    }

    @Test
    void 노선_ID로_노선을_삭제한다() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        final Long id = lineService.createLine(lineCreateRequest);

        lineService.removeLine(id);

        final List<LineResponse> lineResponses = lineService.findAll();
        assertThat(lineResponses.isEmpty()).isTrue();
    }
}
