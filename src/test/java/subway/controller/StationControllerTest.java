package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.service.StationService;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("registerStation() : station이 정상적으로 저장되면 201 Created를 반환한다.")
    void test_registerStation() throws Exception {
        //given
        final String currentStationName = "A";
        final String nextStationName = "B";
        final String lineName = "line";
        final int distance = 4;

        final StationRegisterRequest stationRegisterRequest =
                new StationRegisterRequest(lineName, currentStationName, nextStationName, distance);

        final String bodyData = objectMapper.writeValueAsString(stationRegisterRequest);

        doNothing().when(stationService)
                   .registerStation(any());

        //when & then
        mockMvc.perform(post("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyData))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("deleteStation() : station 이 정상적으로 삭제되면 204 No Content 를 반환한다.")
    void test_deleteStation() throws Exception {
        //given
        final String lineName = "line";
        final String stationName = "station";

        final StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(lineName, stationName);

        final String bodyData = objectMapper.writeValueAsString(stationDeleteRequest);

        doNothing().when(stationService)
                   .deleteStation(any());

        //when & then
        mockMvc.perform(delete("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyData))
               .andExpect(status().isNoContent());
    }
}
