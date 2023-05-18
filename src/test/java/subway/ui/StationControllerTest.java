package subway.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@WebMvcTest(StationController.class)
class StationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private StationService stationService;
    
    @Test
    @DisplayName("역 생성 테스트")
    void createStationMvcTest() throws Exception {
        // given
        final StationRequest stationRequest = new StationRequest("테스트역");
        final StationResponse stationResponse = new StationResponse(1L, "테스트역");
        // when
        when(this.stationService.saveStation(stationRequest)).thenReturn(stationResponse);
        // then
        this.mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(stationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("테스트역"));
    }
    
    @Test
    @DisplayName("역 조회 테스트")
    void showStationMvcTest() throws Exception {
        // given
        final StationResponse stationResponse = new StationResponse(1L, "테스트역");
        // when
        when(this.stationService.findStationResponseById(1L)).thenReturn(stationResponse);
        // then
        this.mockMvc.perform(get("/stations/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("테스트역"));
    }
    
    @Test
    @DisplayName("역 목록 조회 테스트")
    void showStationsMvcTest() throws Exception {
        // given
        final StationResponse stationResponse = new StationResponse(1L, "테스트역");
        // when
        when(this.stationService.findAllStationResponses()).thenReturn(List.of(stationResponse));
        // then
        this.mockMvc.perform(get("/stations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("테스트역"));
    }
    
}