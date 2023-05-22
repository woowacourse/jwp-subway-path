package subway.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.core.service.LineService;
import subway.application.core.service.dto.out.StationResult;
import subway.presentation.controller.LineController;
import subway.presentation.dto.StationEnrollRequest;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("/subway/{lineId}로 POST 요청을 보낼 수 있다")
    void enrollStation() throws Exception {
        // given
        StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(1L, 2L, 1);
        String body = objectMapper.writeValueAsString(stationEnrollRequest);
        doNothing().when(lineService).enrollStation(any());

        // when
        mockMvc.perform(post("/subway/{lineId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))

                // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("/subway/stations/{lineId}로 DELETE 요청을 보낼 수 있다")
    void deleteStation() throws Exception {
        // given
        doNothing().when(lineService).deleteStation(any());
        Integer lineId = 1;

        // when
        mockMvc.perform(delete("/subway/{lineId}/stations/{stationId}", lineId, 2))

                // then
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/line/" + lineId));
    }

    @Test
    @DisplayName("/subway/{lineId}로 GET 요청을 보낼 수 있다")
    void getRouteMap() throws Exception {
        // given
        when(lineService.findRouteMap(any())).thenReturn(List.of(
                new StationResult(1L, "잠실역"),
                new StationResult(2L, "방배역")
        ));
        Integer lineId = 1;

        // when
        mockMvc.perform(get("/subway/{lineId}", lineId))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists());
    }

    @Test
    @DisplayName("/subway로 GET 요청을 보낼 수 있다")
    void getAllRouteMap() throws Exception {
        // given
        when(lineService.findAllRouteMap()).thenReturn(Map.of(
                "1호선", List.of(
                        new StationResult(1L, "수원역"),
                        new StationResult(2L, "세류역")),
                "2호선", List.of(
                        new StationResult(3L, "잠실역"),
                        new StationResult(4L, "방배역")
                )
        ));

        // when
        mockMvc.perform(get("/subway"))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].[*].id").exists())
                .andExpect(jsonPath("$.[*].[*].name").exists());
    }
}
