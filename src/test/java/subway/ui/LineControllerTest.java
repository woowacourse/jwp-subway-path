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
import subway.dto.LineResponse;
import subway.dto.SectionRequest;

import java.util.List;

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
    @DisplayName("POST /lines/{id}/stations/init")
    void createInitialSection() throws Exception {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        final String request = objectMapper.writeValueAsString(sectionRequest);
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "green", List.of("A", "B", "C"));
        given(lineService.saveInitialSection(anyLong(), any())).willReturn(lineResponse);

        // when & then
        mockMvc.perform(post("/lines/1/stations/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/stations/init/1"));

        verify(lineService, times(1)).saveInitialSection(anyLong(), any());
    }

    @Test
    @DisplayName("POST /lines/{id}/stations")
    void createSection() throws Exception {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        final String request = objectMapper.writeValueAsString(sectionRequest);
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "green", List.of("A", "B", "C"));
        given(lineService.saveSection(anyLong(), any())).willReturn(lineResponse);

        // when & then
        mockMvc.perform(post("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/stations/1"));

        verify(lineService, times(1)).saveSection(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /lines/{id}/stations/{stationId}")
    void deleteStationInLine() throws Exception {
        // given
        doNothing().when(lineService).deleteStationById(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/lines/1/stations/1"))
                .andExpect(status().isNoContent());
        verify(lineService, times(1)).deleteStationById(anyLong(), anyLong());
    }
}
