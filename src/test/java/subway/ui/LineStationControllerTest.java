package subway.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineStationService;
import subway.dto.LineStationAddRequest;
import subway.dto.LineStationInitRequest;

@WebMvcTest(LineStationController.class)
class LineStationControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LineStationService lineStationService;

    @Test
    @DisplayName("노선에 초기 역을 정상 생성한다")
    void createInitialStations() throws Exception {

        mockMvc.perform(post("/lines/1/stations/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineStationInitRequest("강남역", "선릉역", 10))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("노선에 추가된 역을 정상 생성한다")
    void createAdditionalStation() throws Exception {
        mockMvc.perform(post("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineStationAddRequest("강남역", "선릉역", "UP", 10))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("노선의 역을 정상 삭제한다")
    void deleteStation() throws Exception {

        mockMvc.perform(delete("/lines/1/stations/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
