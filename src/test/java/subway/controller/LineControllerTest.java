package subway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.LineService;
import subway.domain.exception.EmptySectionException;
import subway.domain.exception.NoSuchStationException;
import subway.dto.SectionRequest;
import subway.dto.StationDeleteRequest;
import subway.ui.LineController;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @DisplayName("노선에 구간을 추가한다")
    @Test
    void addSection() throws Exception {
        String body = objectMapper.writeValueAsString(
                new SectionRequest(
                        2L,
                        1L,
                        10
                )
        );

        this.mockMvc.perform(post("/lines/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @DisplayName("구간 등록에 실패한다")
    @Test
    void addSectionFails() throws Exception {
        String body = objectMapper.writeValueAsString(
                new SectionRequest(2L, 1L, 10));
        doThrow(new EmptySectionException()).when(lineService).addSection(anyLong(), any());

        this.mockMvc.perform(post("/lines/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("노선의 역을 제거한다")
    @Test
    void deleteStation() throws Exception {
        String body = objectMapper.writeValueAsString(
                new StationDeleteRequest(1L));

        this.mockMvc.perform(delete("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }

    @DisplayName("노선 역 제거에 실패한다")
    @Test
    void deleteStations() throws Exception {
        String body = objectMapper.writeValueAsString(
                new StationDeleteRequest(1L));
        doThrow(new NoSuchStationException()).when(lineService).deleteStation(anyLong(), any());

        this.mockMvc.perform(delete("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
