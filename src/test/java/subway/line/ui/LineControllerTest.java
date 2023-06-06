package subway.line.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.common.webmvc.AbstractControllerTest;
import subway.line.dto.request.LineCreateRequest;
import subway.line.dto.request.LineCreateRequestDto;
import subway.line.dto.request.LineUpdateInfoRequest;
import subway.line.dto.response.InterStationResponseDto;
import subway.line.dto.response.LineResponseDto;
import subway.line.dto.response.LinesResponseDto;

@DisplayNameGeneration(ReplaceUnderscores.class)
class LineControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_생성된다() throws Exception {
        LineResponseDto givenResult = new LineResponseDto(1L, "2호선", "초록색",
                List.of(new InterStationResponseDto(1L, 2L, 3L, 4L)));
        given(lineCommandService.createLine(any(LineCreateRequest.class)))
                .willReturn(givenResult);
        String requestBody = objectMapper.writeValueAsString(new LineCreateRequestDto("2호선", "초록색", 1L, 2L, 10L));
        String expect = objectMapper.writeValueAsString(new LineResponseDto(1L, "2호선", "초록색", List.of(
                new InterStationResponseDto(1L, 2L, 3L, 4L))));

        mockMvc.perform(post("/lines")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/1"))
                .andExpect(content().json(expect));
    }

    @Test
    void 정상적으로_삭제된다() throws Exception {
        mockMvc.perform(delete("/lines/1"))

                .andExpect(status().isNoContent());
    }

    @Test
    void 정상적으로_전체가_조회된다() throws Exception {
        given(lineQueryService.findAllLines())
                .willReturn(List.of(
                        new LineResponseDto(1L, "2호선", "초록색",
                                List.of(new InterStationResponseDto(1L, 2L, 3L, 4L))),
                        new LineResponseDto(2L, "신분당선", "빨간색",
                                List.of(new InterStationResponseDto(1L, 2L, 3L, 4L)))
                ));
        var result = new LinesResponseDto(List.of(
                new LineResponseDto(1L, "2호선", "초록색",
                        List.of(new InterStationResponseDto(1L, 2L, 3L, 4L))),
                new LineResponseDto(2L, "신분당선", "빨간색",
                        List.of(new InterStationResponseDto(1L, 2L, 3L, 4L)))
        ));
        String expect = objectMapper.writeValueAsString(result);

        mockMvc.perform(get("/lines"))

                .andExpect(status().isOk())
                .andExpect(content().json(expect));
    }

    @Test
    void 정상적으로_조회된다() throws Exception {
        given(lineQueryService.findById(1L))
                .willReturn(new LineResponseDto(1L, "2호선", "green", List.of(
                        new InterStationResponseDto(1L, 2L, 3L, 4L)
                )));
        LineResponseDto resultBody = new LineResponseDto(1L, "2호선", "green", List.of(
                new InterStationResponseDto(1L, 2L, 3L, 4L)
        ));
        String expect = objectMapper.writeValueAsString(resultBody);

        mockMvc.perform(get("/lines/1"))

                .andExpect(status().isOk())
                .andExpect(content().json(expect));
    }

    @Test
    void 정상적으로_수정된다() throws Exception {
        String request = objectMapper.writeValueAsString(new LineUpdateInfoRequest("2호선", "초록색"));

        mockMvc.perform(patch("/lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))

                .andExpect(status().isNoContent());
    }
}
