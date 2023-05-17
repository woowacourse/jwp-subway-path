package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("POST /lines/{id}/station/init")
    void createInitialSection() throws Exception {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        final String request = objectMapper.writeValueAsString(sectionRequest);
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 10);
        given(lineService.saveInitialSection(anyLong(), any())).willReturn(sectionResponse);

        // when & then
        mockMvc.perform(post("/lines/1/station/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/station/init/1"));

        verify(lineService, times(1)).saveInitialSection(anyLong(), any());
    }

    @Test
    @DisplayName("POST /lines/{id}/station")
    void createSection() throws Exception {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        final String request = objectMapper.writeValueAsString(sectionRequest);
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 10);
        given(lineService.saveSection(anyLong(), any())).willReturn(sectionResponse);

        // when & then
        mockMvc.perform(post("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/station/1"));

        verify(lineService, times(1)).saveSection(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /lines/{id}/station/{stationId}")
    void deleteStationInLine() throws Exception {
        // given
        doNothing().when(lineService).deleteStationById(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/lines/1/station/1"))
                .andExpect(status().isNoContent());
        verify(lineService, times(1)).deleteStationById(anyLong(), anyLong());
    }
}
