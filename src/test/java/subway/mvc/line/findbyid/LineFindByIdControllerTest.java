package subway.mvc.line.findbyid;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.application.port.in.InterStationResponseDto;
import subway.line.application.port.in.LineResponseDto;
import subway.line.ui.dto.in.InterStationResponse;
import subway.line.ui.dto.in.LineResponse;
import subway.mvc.AbstractControllerTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 조회하는 기능 mvc 테스트")
class LineFindByIdControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_조회된다() throws Exception {
        given(lineFindByIdUseCase.findById(1L))
                .willReturn(new LineResponseDto(1L, "2호선", "green", List.of(
                        new InterStationResponseDto(1L, 2L, 3L, 4L)
                )));
        LineResponse resultBody = new LineResponse(1L, "2호선", "green", List.of(
                new InterStationResponse(1L, 2L, 3L, 4L)
        ));
        String expect = objectMapper.writeValueAsString(resultBody);

        mockMvc.perform(get("/lines/1"))

                .andExpect(status().isOk())
                .andExpect(content().json(expect));
    }
}
