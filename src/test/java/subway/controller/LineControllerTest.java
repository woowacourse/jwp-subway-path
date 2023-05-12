package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.dto.StationDeleteRequest;
import subway.dto.StationRegisterRequest;
import subway.dto.StationsRegisterRequest;
import subway.ui.LineController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @DisplayName("노선에 역을 등록한다")
    @Test
    void registerStation() throws Exception {
        String body = objectMapper.writeValueAsString(
                new StationRegisterRequest(
                        "UPPER",
                        2L,
                        1L,
                        10
                )
        );

        this.mockMvc.perform(post("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @DisplayName("노선에 역을 두 개 등록한다")
    @Test
    void registerStations() throws Exception {
        String body = objectMapper.writeValueAsString(
                new StationsRegisterRequest(2L,1L,10));

        this.mockMvc.perform(post("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @DisplayName("노선에 역을 제거한다")
    @Test
    void deleteStations() throws Exception {
        String body = objectMapper.writeValueAsString(
                new StationDeleteRequest(1L));

        this.mockMvc.perform(delete("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }
}
