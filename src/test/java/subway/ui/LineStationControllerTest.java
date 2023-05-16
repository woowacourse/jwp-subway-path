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
import subway.dto.request.ConnectRequest;
import subway.dto.response.LineStationResponse;
import subway.dto.response.StationResponse;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineStationController.class)
class LineStationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LineStationService lineStationService;

    @Test
    @DisplayName("patch /lines/{lineId}/stations/{stationId}?type=init : noContent를 반환한다.")
    void connectStationsInit() throws Exception {
        // given
        ConnectRequest request = new ConnectRequest(null, 2L, 1);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1?type=init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());

        verify(lineStationService, times(1))
                .addInitStations(
                        1L,
                        1L,
                        request.getNextStationId(),
                        request.getDistance()
                );
    }

    @Test
    @DisplayName("patch /lines/{lineId}/stations/{stationId}?type=up : noContent를 반환한다.")
    void connectStationsUp() throws Exception {
        // given
        ConnectRequest request = new ConnectRequest(null, 2L, 1);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1?type=up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());

        verify(lineStationService, times(1))
                .addUpEndpoint(
                        1L,
                        1L,
                        request.getDistance()
                );
    }

    @Test
    @DisplayName("patch /lines/{lineId}/stations/{stationId}?type=down : noContent를 반환한다.")
    void connectStationsDown() throws Exception {
        // given
        ConnectRequest request = new ConnectRequest(2L, null, 1);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1?type=down")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());

        verify(lineStationService, times(1))
                .addDownEndpoint(
                        1L,
                        1L,
                        request.getDistance()
                );
    }

    @Test
    @DisplayName("patch /lines/{lineId}/stations/{stationId}?type=mid : noContent를 반환한다.")
    void connectStationsMid() throws Exception {
        // given
        ConnectRequest request = new ConnectRequest(2L, 3L, 1);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1?type=mid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());

        verify(lineStationService, times(1))
                .addIntermediate(
                        1L,
                        1L,
                        request.getPrevStationId(),
                        request.getDistance()
                );
    }

    @Test
    @DisplayName("get /lines/{lineId}/stations : 라인에 해당하는 모든 역을 조회한다.")
    void showStationsByLineId() throws Exception {
        // given
        List<String> stations = List.of("선릉역", "강남역", "잠실역");
        List<Integer> distances = List.of(1, 3);
        LineStationResponse response = new LineStationResponse(stations, distances);
        String jsonResponse = objectMapper.writeValueAsString(response);

        when(lineStationService.findByLineId(1L)).thenReturn(response);


        // when & then
        mockMvc.perform(get("/lines/1/stations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        verify(lineStationService, times(1)).findByLineId(1L);
    }
}