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
import subway.dto.request.ConnectionEndpointRequest;
import subway.dto.request.ConnectionInitRequest;
import subway.dto.request.ConnectionMidRequest;
import subway.dto.response.LineStationResponse;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    @DisplayName("patch /lines/{lineId}/stations/{stationId}/init : noContent를 반환한다.")
    void addStationToLine_init() throws Exception {
        // given
        final ConnectionInitRequest request = new ConnectionInitRequest(2L, 1);
        final String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1/init")
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
    @DisplayName("patch /lines/{lineId}/stations/{stationId}/endpoint : noContent를 반환한다.")
    void addStationToLine_up() throws Exception {
        // given
        final ConnectionEndpointRequest request = new ConnectionEndpointRequest(EndpointType.UP, 1);
        final String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1/endpoint")
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
    @DisplayName("patch /lines/{lineId}/stations/{stationId}/endpoint : noContent를 반환한다.")
    void addStationToLine_down() throws Exception {
        // given
        final ConnectionEndpointRequest request = new ConnectionEndpointRequest(EndpointType.DOWN, 1);
        final String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1/endpoint")
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
    @DisplayName("patch /lines/{lineId}/stations/{stationId}/mid : noContent를 반환한다.")
    void addStationToLine_mid() throws Exception {
        // given
        final ConnectionMidRequest request = new ConnectionMidRequest(2L, 1);
        final String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/lines/1/stations/1/mid")
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

    @DisplayName("lineId와 stationId에 해당하는 역 연결 정보를 삭제한다")
    @Test
    void deleteStationById() throws Exception {
        mockMvc.perform(delete("/lines/1/stations/1"))
                .andExpect(status().isNoContent());

        verify(lineStationService, times(1)).deleteStationInLine(1L, 1L);
    }

    @Test
    @DisplayName("get /lines/{lineId}/stations : 라인에 해당하는 모든 역을 조회한다.")
    void showStationsByLineId() throws Exception {
        // given
        final List<String> stations = List.of("선릉역", "강남역", "잠실역");
        final List<Integer> distances = List.of(1, 3);
        final LineStationResponse response = new LineStationResponse(stations, distances);
        final String jsonResponse = objectMapper.writeValueAsString(response);

        when(lineStationService.findByLineId(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/lines/1/stations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        verify(lineStationService, times(1)).findByLineId(1L);
    }

    @DisplayName("get /lines/stations : 모든 역을 조회한다")
    @Test
    void showStations() throws Exception {
        // given
        final List<String> stations1 = List.of("선릉역", "강남역", "잠실역");
        final List<Integer> distances1 = List.of(1, 3);
        final List<String> stations2 = List.of("구의역", "건대입구역", "신도림역");
        final List<Integer> distances2 = List.of(5, 8);
        final LineStationResponse response = new LineStationResponse(stations1, distances1);
        final LineStationResponse response2 = new LineStationResponse(stations2, distances2);
        final List<LineStationResponse> responses = List.of(response, response2);
        final String jsonResponse = objectMapper.writeValueAsString(responses);

        when(lineStationService.findAll()).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/lines/stations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse));
    }
}