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
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    @MockBean
    private SectionService sectionService;

    @Test
    @DisplayName("POST /lines/{id}/station/init")
    void createInitialSection() throws Exception {
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        final String request = objectMapper.writeValueAsString(sectionRequest);
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 10);
        given(sectionService.saveInitialSection(any())).willReturn(sectionResponse);

        mockMvc.perform(post("/lines/1/station/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/station/init/1"));

        verify(sectionService, times(1)).saveInitialSection(any());
    }

    @Test
    @DisplayName("POST /lines/{id}/station")
    void createSection() throws Exception {
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        final String request = objectMapper.writeValueAsString(sectionRequest);
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 10);
        given(sectionService.saveSection(any())).willReturn(sectionResponse);

        mockMvc.perform(post("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/lines/1/station/1"));

        verify(sectionService, times(1)).saveSection(any());
    }
}
