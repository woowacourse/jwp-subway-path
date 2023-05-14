package subway.mvc.line;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.application.line.port.in.InterStationResponseDto;
import subway.application.line.port.in.LineResponseDto;
import subway.mvc.AbstractControllerTest;
import subway.ui.line.dto.in.InterStationResponse;
import subway.ui.line.dto.in.LineResponse;
import subway.ui.line.dto.in.LinesResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 모두 조회하는 기능 mvc 테스트")
class LineFindAllControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_조회된다() throws Exception {
        given(lineFindAllUseCase.findAllLines())
            .willReturn(List.of(
                new LineResponseDto(1L, "2호선", "초록색",
                    List.of(new InterStationResponseDto(1L, 2L, 3L, 4L))),
                new LineResponseDto(2L, "신분당선", "빨간색",
                    List.of(new InterStationResponseDto(1L, 2L, 3L, 4L)))
            ));
        final LinesResponse result = new LinesResponse(List.of(
            new LineResponse(1L, "2호선", "초록색",
                List.of(new InterStationResponse(1L, 2L, 3L, 4L))),
            new LineResponse(2L, "신분당선", "빨간색",
                List.of(new InterStationResponse(1L, 2L, 3L, 4L)))
        ));
        final String expect = objectMapper.writeValueAsString(result);

        mockMvc.perform(get("/lines"))

            .andExpect(status().isOk())
            .andExpect(content().json(expect));
    }
}
