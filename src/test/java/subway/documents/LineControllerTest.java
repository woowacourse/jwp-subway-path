package subway.documents;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class LineControllerTest extends DocumentationSteps {

    @Test
    void 노선을_저장한다() throws Exception {
        // given
        final LineRequest request = new LineRequest("2호선", "초록색", 1L, 4L, 10);

        given(lineService.saveLine(any(LineRequest.class))).willReturn(1L);

        // when, then
        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1"))
                .andDo(document("lines/save"));
    }

    @Test
    void 노선을_전체_조회_한다() throws Exception {
        // given
        final List<StationResponse> 이호선_역 = List.of(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "잠실새내역"),
                new StationResponse(3L, "종합운동장역"));
        final LineResponse 이호선_응답 = new LineResponse(1L, "2호선", "초록색", 이호선_역);

        final List<StationResponse> 팔호선_역 = List.of(
                new StationResponse(1L, "잠실역"),
                new StationResponse(4L, "몽촌토성역"),
                new StationResponse(5L, "천호역"));
        final LineResponse 팔호선_응답 = new LineResponse(2L, "8호선", "분홍색", 팔호선_역);

        final List<LineResponse> result = List.of(이호선_응답, 팔호선_응답);
        given(lineService.findLineResponses()).willReturn(result);

        // when, then
        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(document("lines/findAll"));
    }

    @Test
    void 노선을_단건_조회_한다() throws Exception {
        // given
        final List<StationResponse> 이호선_역 = List.of(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "잠실새내역"),
                new StationResponse(3L, "종합운동장역"));
        final Long lineId = 1L;
        final LineResponse 이호선_응답 = new LineResponse(lineId, "2호선", "초록색", 이호선_역);

        given(lineService.findLineResponseById(anyLong())).willReturn(이호선_응답);

        // when, then
        mockMvc.perform(get("/lines/{lineId}", lineId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(이호선_응답)))
                .andDo(document("lines/findLineById"));
    }

    @Test
    void 노선을_삭제_한다() throws Exception {
        // given
        willDoNothing().given(lineService).deleteLineById(anyLong());

        final Long lineId = 1L;

        // when, then
        mockMvc.perform(delete("/lines/{lineId}", lineId))
                .andExpect(status().isNoContent())
                .andDo(document("lines/delete"));
    }
}
