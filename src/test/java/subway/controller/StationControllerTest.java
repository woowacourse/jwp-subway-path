package subway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.StationService;
import subway.controller.dto.StationRequest;
import subway.controller.dto.StationResponse;

@WebMvcTest(controllers = StationController.class)
class StationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StationService stationService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("역을 등록한다.")
    void createStation() throws Exception {
        given(stationService.saveStation(any()))
                .willReturn(new StationResponse(1L, "서울역"));

        mockMvc.perform(post("/stations")
                        .content(objectMapper.writeValueAsString(new StationRequest("서울역")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("역을 등록할 때 역 이름이 공백이면 400 BAD REQUEST가 발생한다..")
    void createStationFail() throws Exception {
        mockMvc.perform(post("/stations")
                        .content(objectMapper.writeValueAsString(new StationRequest(" ")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("모든 역을 가져온다.")
    void findAllStations() throws Exception {
        given(stationService.findAllStationResponses())
                .willReturn(List.of(new StationResponse(1L, "서울역"), new StationResponse(2L, "부산역")));

        mockMvc.perform(get("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("ID에 해당하는 역을 가져온다.")
    void findStation() throws Exception {
        given(stationService.findStationResponseById(any()))
                .willReturn(new StationResponse(1L, "서울역"));

        mockMvc.perform(get("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("ID에 해당하는 역을 삭제한다.")
    void deleteStation() throws Exception {
        willDoNothing().given(stationService).deleteStationById(any());

        mockMvc.perform(delete("/stations/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
