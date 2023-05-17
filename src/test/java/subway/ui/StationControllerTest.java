package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineStationService;
import subway.application.StationService;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StationService stationService;
    @MockBean
    private LineStationService lineStationService;

    @Test
    @DisplayName("post /stations : created를 반환하고 Location에 uri를 저장한")
    void createStation() throws Exception {
        // given
        final StationRequest request = new StationRequest("잠실역");
        final StationResponse response = new StationResponse(1L, "잠실역");

        final String jsonRequest = objectMapper.writeValueAsString(request);

        when(stationService.saveStation(any())).thenReturn(response);

        // when & then
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/stations/1"));

        verify(stationService, times(1)).saveStation(any());
    }

    @Test
    @DisplayName("get /stations : ok를 반환하고 모든 StationResponse를 반환한다")
    void showStations() throws Exception {
        // given
        final StationResponse response1 = new StationResponse(1L, "잠실역");
        final StationResponse response2 = new StationResponse(2L, "선릉역");
        final StationResponse response3 = new StationResponse(3L, "강남역");
        final List<StationResponse> responses = List.of(response1, response2, response3);
        final String jsonResponse = objectMapper.writeValueAsString(responses);

        when(stationService.findAllStationResponses()).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/stations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        verify(stationService, times(1)).findAllStationResponses();
    }

    @Test
    @DisplayName("get /stations/{id} : ok를 반환하고 id에 해당하는 stationResponse를 반환한다")
    void showStation() throws Exception {
        // given
        final StationResponse response1 = new StationResponse(1L, "잠실역");
        final StationResponse response2 = new StationResponse(2L, "선릉역");
        final StationResponse response3 = new StationResponse(3L, "강남역");
        final String jsonResponse = objectMapper.writeValueAsString(response2);

        when(stationService.findStationResponseById(2L)).thenReturn(response2);

        // when & then
        mockMvc.perform(get("/stations/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        verify(stationService, times(1)).findStationResponseById(2L);
    }

    @Test
    @DisplayName("put /stations/{id} : ok를 반환한다")
    void updateStation() throws Exception {
        // given
        final StationRequest request = new StationRequest("선릉역");

        final String jsonRequest = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(put("/stations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(stationService, times(1)).updateStation(any(), any());
    }

    @Test
    @DisplayName("delete /stations/{id} : noContent를 반환한다")
    void deleteStation() throws Exception {
        // when & then
        mockMvc.perform(delete("/stations/1"))
                .andExpect(status().isNoContent());

        verify(lineStationService, times(1)).deleteStation(1L);
    }
}