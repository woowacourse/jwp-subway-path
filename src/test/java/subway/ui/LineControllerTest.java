package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.LineSectionResponse;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LineService lineService;

    @DisplayName("노선 정상 생성한다")
    @Test
    void createLine() throws Exception {
        given(lineService.saveLine(any()))
                .willReturn(new LineResponse(1L, "2호선", "green"));

        mockMvc.perform(post("/lines")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(new LineRequest("2호선", "green"))))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", "/lines/1"))
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("2호선"))
               .andExpect(jsonPath("$.color").value("green"))
               .andDo(print());
    }

    @DisplayName("전체 노선을 조회한다")
    @Test
    void findAllLines() throws Exception {
        List<LineSectionResponse> lines = List.of(
                LineSectionResponse.from(1L, "2호선", "green", Collections.emptyList()),
                LineSectionResponse.from(2L, "1호선", "blue", List.of("동인천역", "주안역")));
        given(lineService.findLineResponses())
                .willReturn(lines);

        //todo 처음 배운 정보 기록1 : Matchers.empty() 메서드는 Hamcrest 라이브러리의 Matcher 클래스의 정적 메서드이며, 빈 리스트(empty list)인 경우에 true를 반환합니다.
        mockMvc.perform(get("/lines"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].name").value("2호선"))
               .andExpect(jsonPath("$[0].color").value("green"))
               .andExpect(jsonPath("$[0].stations").value(Matchers.empty()))
               .andExpect(jsonPath("$[1].id").value(2L))
               .andExpect(jsonPath("$[1].name").value("1호선"))
               .andExpect(jsonPath("$[1].color").value("blue"))
               .andExpect(jsonPath("$[1].stations[0]").value("동인천역"))
               .andExpect(jsonPath("$[1].stations[1]").value("주안역"))
               .andDo(print());
    }

    @DisplayName("특정 노선을 조회한다")
    @Test
    void findLineById() throws Exception {
        Long lineId = 1L;
        given(lineService.findLineResponseById(anyLong()))
                .willReturn(LineSectionResponse.from(lineId, "1호선", "blue", List.of("동인천역", "주안역")));
        mockMvc.perform(get("/lines/" + lineId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("1호선"))
               .andExpect(jsonPath("$.color").value("blue"))
               .andExpect(jsonPath("$.stations[0]").value("동인천역"))
               .andExpect(jsonPath("$.stations[1]").value("주안역"))
               .andDo(print());
    }

    // todo : 수정에 성공했을 때 이를 클라이언트에게도 반환값을 통해 알릴 것인가?
    @DisplayName("특정 노선의 정보를 수정한다")
    @Test
    void updateLine() throws Exception {
        Long lineId = 1L;
        mockMvc.perform(put("/lines/" + lineId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(new LineRequest("2호선", "green"))))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @DisplayName("특정 노선을 삭제한다")
    @Test
    void deleteLine() throws Exception {
        Long lineId = 1L;
        mockMvc.perform(delete("/lines/" + lineId))
               .andExpect(status().isNoContent())
               .andDo(print());
    }
}
