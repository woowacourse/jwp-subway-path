package subway.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LineService lineService;

    @Test
    @DisplayName("노선 정상 생성한다")
    void createLine() throws Exception {
        given(lineService.createLine(any()))
                .willReturn(new Line(1L, "2호선", "green", 0, new Sections(new ArrayList<>())));

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineRequest("2호선", "green", 0))))
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
        List<Line> lines = List.of(
                new Line(1L, "2호선", "green", 0, new Sections(new ArrayList<>())),
                new Line(2L, "1호선", "blue", 0,
                        new Sections(
                                List.of(new Section(new Station("동인천역"), new Station("주안역"), new Distance(10))))));
        given(lineService.findLines())
                .willReturn(lines);

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
        given(lineService.findLineById(anyLong()))
                .willReturn(new Line(lineId, "1호선", "blue", 0,
                        new Sections(
                                List.of(new Section(new Station("동인천역"), new Station("주안역"), new Distance(10)))
                        )));
        mockMvc.perform(get("/lines/"+lineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("1호선"))
                .andExpect(jsonPath("$.color").value("blue"))
                .andExpect(jsonPath("$.stations[0]").value("동인천역"))
                .andExpect(jsonPath("$.stations[1]").value("주안역"))
                .andDo(print());
    }

    @DisplayName("특정 노선의 정보를 수정한다")
    @Test
    void updateLine() throws Exception {
        Long lineId = 1L;
        mockMvc.perform(put("/lines/" + lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineRequest("2호선", "green",0))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("특정 노선을 삭제한다")
    @Test
    void deleteLine() throws Exception {
        Long lineId = 1L;
        mockMvc.perform(delete("/lines/"+ lineId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}
