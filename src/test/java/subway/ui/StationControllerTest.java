package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.StationService;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StationService stationService;

    @DisplayName("역이 정상 생성된다")
    @Test
    void createStation() throws Exception {
        given(stationService.saveStation(any()))
                .willReturn(new StationResponse(1L, "서울대입구"));

        mockMvc.perform(post("/stations")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(new StationRequest("서울대입구"))))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", "/stations/1"))
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("서울대입구"))
               .andDo(print());
    }

    @DisplayName("전체 역을 조회한다")
    @Test
    void showStations() throws Exception {
        List<StationResponse> responseValues = List.of(
                new StationResponse(1L, "서울대입구"),
                new StationResponse(2L, "봉천역"),
                new StationResponse(3L, "낙성대역"),
                new StationResponse(4L, "사당역")
        );
        given(stationService.findAllStationResponses())
                .willReturn(responseValues);

        mockMvc.perform(get("/stations"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].name").value("서울대입구"))
               .andExpect(jsonPath("$[1].id").value(2L))
               .andExpect(jsonPath("$[1].name").value("봉천역"))
               .andExpect(jsonPath("$[2].id").value(3L))
               .andExpect(jsonPath("$[2].name").value("낙성대역"))
               .andExpect(jsonPath("$[3].id").value(4L))
               .andExpect(jsonPath("$[3].name").value("사당역"))
               .andDo(print());
    }

    @DisplayName("특정 역을 조회한다")
    @Test
    void showStation() throws Exception {
        StationResponse stationResponse = new StationResponse(1L, "서울대입구");
        given(stationService.findStationResponseById(stationResponse.getId()))
                .willReturn(stationResponse);

        mockMvc.perform(get("/stations/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("서울대입구"))
               .andDo(print());
    }

    @DisplayName("특정 역을 수정할 수 있다")
    @Test
    void updateStation() throws Exception {
        StationRequest stationRequest = new StationRequest("사당");
        willDoNothing().given(stationService)
                       .updateStation(anyLong(), any());

        mockMvc.perform(put("/stations/1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsBytes(stationRequest)))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @DisplayName("특정 역을 삭제할 수 있다")
    @Test
    void deleteStation() throws Exception {
        willDoNothing().given(stationService)
                       .deleteStationById(anyLong());

        mockMvc.perform(delete("/stations/1"))
               .andExpect(status().isNoContent())
               .andDo(print());
    }
}
