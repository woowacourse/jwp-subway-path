package subway.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.LineService;
import subway.dto.StationIdRequest;
import subway.ui.LineController;

@WebMvcTest(LineController.class)
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @DisplayName("노선에 역을 등록한다")
    @Test
    void registerStations() throws Exception {
        this.mockMvc.perform(post("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(new StationIdRequest(1L), new StationIdRequest(2L)))))
                .andExpect(status().isOk());
    }

    @DisplayName("노선에 역을 제거한다")
    @Test
    void deleteStations() throws Exception {
        this.mockMvc.perform(delete("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(new StationIdRequest(1L), new StationIdRequest(2L)))))
                .andExpect(status().isNoContent());
    }
}
