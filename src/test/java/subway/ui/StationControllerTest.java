package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.StationService;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationInitialCreateRequest;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StationService stationService;

    @Test
    @DisplayName("노선에 초기 역을 정상 생성한다")
    void createInitialStations() throws Exception {
        given(stationService.createInitialStations(anyLong(), any()))
                .willReturn(List.of(new Station(1L, "강남역"), new Station(2L, "선릉역")));

        mockMvc.perform(post("/lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationInitialCreateRequest("강남역", "선릉역", 10))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("강남역"))
                .andExpect(jsonPath("$[1].name").value("선릉역"))
                .andDo(print());
    }

    @Test
    @DisplayName("노선에 추가된 역을 정상 생성한다")
    void createAdditionalStation() throws Exception {
        given(stationService.createStation(anyLong(), any()))
                .willReturn(new Station(1L, "강남역"));

        mockMvc.perform(post("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationCreateRequest("강남역", "선릉역", "UP", 10))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("강남역"))
                .andDo(print());
    }

    @Test
    @DisplayName("노선의 역을 정상 삭제한다")
    void deleteStation() throws Exception {
        mockMvc.perform(delete("/lines/1/stations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
