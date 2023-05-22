package subway.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.dto.LineResponse;

@WebMvcTest(LineController.class)
class LineControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private LineService lineService;
    
    @Test
    @DisplayName("모든 호선 조회 테스트")
    void findAllLinesMvcTest() throws Exception {
        when(this.lineService.findLineResponses()).thenReturn(List.of(
                new LineResponse(1L, "2호선", "초록색"),
                new LineResponse(2L, "3호선", "오렌지색"),
                new LineResponse(3L, "신분당선", "빨간색")
        ));
        this.mockMvc.perform(get("/lines"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("2호선"))
                .andExpect(jsonPath("$[0].color").value("초록색"));
        
        this.mockMvc.perform(get("/lines"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("3호선"))
                .andExpect(jsonPath("$[1].color").value("오렌지색"));
    }
    
    @Test
    @DisplayName("호선 조회 테스트")
    void findLineByIdMvcTest() throws Exception {
        when(this.lineService.findLineResponseById(1L)).thenReturn(new LineResponse(1L, "2호선", "초록색"));
        this.mockMvc.perform(get("/lines/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("2호선"))
                .andExpect(jsonPath("$.color").value("초록색"));
    }
}