package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.service.StationService;
import subway.business.service.dto.StationRequest;
import subway.business.service.dto.StationResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
public class StationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @DisplayName("역을 생성한다.")
    @Test
    void shouldCreateStationWhenRequest() throws Exception {
        given(stationService.saveStation(any())).willReturn(1L);
        StationRequest stationRequest = new StationRequest("강남역");
        String jsonRequest = objectMapper.writeValueAsString(stationRequest);

        mockMvc.perform(post("/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isCreated());
    }

    @DisplayName("Id를 통해 역 정보를 반환한다.")
    @Test
    void shouldReturnStationResponseWhenAddIdToPath() throws Exception {
        given(stationService.findStationResponseById(1L)).willReturn(new StationResponse(1L, "강남역"));

        mockMvc.perform(get("/stations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("강남역"));
    }

    @DisplayName("모든 역 정보를 반환한다.")
    @Test
    void shouldReturnAllStationResponseWhenRequest() throws Exception {
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "강남역"));
        given(stationService.findAllStationResponses()).willReturn(stationResponses);

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("강남역"));
    }

    @DisplayName("역을 갱신한다.")
    @Test
    void shouldUpdateStationWhenRequest() throws Exception {
        doNothing().when(stationService).updateStation(any(), any());
        StationRequest stationRequest = new StationRequest("역삼역");
        String jsonRequest = objectMapper.writeValueAsString(stationRequest);

        mockMvc.perform(get("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenRequest() throws Exception {
        doNothing().when(stationService).deleteStationById(1L);
        StationRequest stationRequest = new StationRequest("역삼역");
        String jsonRequest = objectMapper.writeValueAsString(stationRequest);

        mockMvc.perform(delete("/stations/1"))
                .andExpect(status().isNoContent());
    }

}
