package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import subway.application.LineService;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.StationRequest;
import subway.exception.LineNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @MockBean
    private LineService lineService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("노선들의 전체 정보를 조회한다.")
    @Test
    void showAllLines() throws Exception {
        given(lineService.findLines())
                .willReturn(List.of(new Line(1L, "1호선", "blue"),
                        new Line(2L, "2호선", "red")));

        mockMvc.perform(get("/lines"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.[0].name", equalTo("1호선")));
    }

    @DisplayName("신규 노선을 추가한다")
    @Test
    void saveLine() throws Exception {
        given(lineService.saveLine(any()))
                .willReturn(1L);

        final LineRequest lineRequest = new LineRequest("경의중앙선", "yellow");

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lineRequest)))
                .andExpect(header().string("location", equalTo("/lines/1")))
                .andExpect(status().isCreated());
    }

    @DisplayName("노선의 모든 역을 조회한다.")
    @Test
    void findAllStationsFromLine() throws Exception {
        given(lineService.findAllStations(any()))
                .willReturn(List.of(new Station(1L, "사당"),
                        new Station(2L, "강남")));

        mockMvc.perform(get("/lines/1/stations"))
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("강남", "사당")));
    }

    @DisplayName("존재하지 않는 노선을 조회할 때 404 발생")
    @Test
    void findAllStationsFromLineException() throws Exception {
        given(lineService.findAllStations(any()))
                .willThrow(new LineNotFoundException());

        mockMvc.perform(get("/lines/1/stations"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("노선에 역을 등록한다")
    @Test
    void addStation() throws Exception {
        given(lineService.addStation(any(), any()))
                .willReturn(1L);

        mockMvc.perform(post("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationRequest())))
                .andExpect(header().string("location", equalTo("/lines/1/stations/1")))
                .andExpect(status().isCreated());
    }

    @DisplayName("노선에서 역을 삭제할 수 있다")
    @Test
    void deleteStation() throws Exception {
        mockMvc.perform(delete("/lines/1/stations/1"))
                .andExpect(status().isNoContent());
    }
}