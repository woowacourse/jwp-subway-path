package subway.mvc.line.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.application.port.in.InterStationResponseDto;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.create.LineCreateRequestDto;
import subway.line.ui.dto.in.InterStationResponse;
import subway.line.ui.dto.in.LineResponse;
import subway.mvc.AbstractControllerTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 생성하는 기능 mvc 테스트")
class LineCreateControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_생성된다() throws Exception {
        LineResponseDto givenResult = new LineResponseDto(1L, "2호선", "초록색",
                List.of(new InterStationResponseDto(1L, 2L, 3L, 4L)));
        given(lineCreateUseCase.createLine(any(LineCreateRequestDto.class)))
                .willReturn(givenResult);
        String requestBody = objectMapper.writeValueAsString(new LineCreateRequestDto("2호선", "초록색", 1L, 2L, 10L));
        String expect = objectMapper.writeValueAsString(new LineResponse(1L, "2호선", "초록색", List.of(
                new InterStationResponse(1L, 2L, 3L, 4L))));

        mockMvc.perform(post("/lines")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/1"))
                .andExpect(content().json(expect));
    }
}
