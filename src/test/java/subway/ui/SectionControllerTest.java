package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SectionService;
import subway.dto.SectionRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(SectionController.class)
public class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SectionService sectionService;

    @Test
    void 구간을_등록한다() throws Exception {
        // given
        final Long lineId = 1L;
        given(sectionService.insertSection(anyLong(), any(SectionRequest.class))).willReturn(lineId);

        final SectionRequest request = new SectionRequest(10, 1L, 2L);
        final String body = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/lines/{lineId}/stations", lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/sections/1"));
    }

    @Test
    void 구간을_삭제한다() throws Exception {
        // given
        final Long lineId = 1L;
        final Long stationId = 2L;
        willDoNothing().given(sectionService)
                .deleteStation(lineId, stationId);

        // when, then
        mockMvc.perform(delete("/lines/{lineId}/stations/{stationId}", lineId, stationId))
                .andExpect(status().isNoContent());
    }
}
