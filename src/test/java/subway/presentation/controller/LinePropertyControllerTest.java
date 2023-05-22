package subway.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.core.service.LinePropertyService;
import subway.application.core.service.dto.out.LinePropertyResult;
import subway.presentation.dto.LineRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinePropertyController.class)
public class LinePropertyControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LinePropertyService linePropertyService;

    @Test
    @DisplayName("/lines 로 POST 요청을 보낼 시, 생성된 노선을 응답한다")
    void createLine() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("1호선", "파랑");
        LinePropertyResult linePropertyResult = new LinePropertyResult(1L, "1호선", "파랑");

        String body = objectMapper.writeValueAsString(lineRequest);
        when(linePropertyService.saveLineProperty(any())).thenReturn(linePropertyResult);

        // when
        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))

                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.color").isNotEmpty());
    }

    @Test
    @DisplayName("/lines 로 GET 요청을 보낼 시, 모든 노선을 응답한다")
    void findAllLines() throws Exception {
        // given
        List<LinePropertyResult> linePropertyResults = List.of(
                new LinePropertyResult(1L, "1호선", "파랑"),
                new LinePropertyResult(2L, "2호선", "초록")
        );

        when(linePropertyService.findAllLineProperty()).thenReturn(linePropertyResults);

        // when
        mockMvc.perform(get("/lines"))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].name").isNotEmpty())
                .andExpect(jsonPath("$[*].color").isNotEmpty());
    }

    @Test
    @DisplayName("/lines/{id} 로 GET 요청을 보낼 시, 특정 노선을 응답한다")
    void findLineById() throws Exception {
        // given
        LinePropertyResult linePropertyResult = new LinePropertyResult(1L, "1호선", "파랑");

        when(linePropertyService.findLinePropertyById(any())).thenReturn(linePropertyResult);

        // when
        mockMvc.perform(get("/lines/{id}", 1L))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.color").isNotEmpty());
    }

    @Test
    @DisplayName("/lines/{id} 로 PUT 요청을 보낼 시, 특정 노선을 수정한다")
    void updateLine() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("1호선", "파랑");

        String body = objectMapper.writeValueAsString(lineRequest);
        doNothing().when(linePropertyService).updateLineProperty(any());

        // when
        mockMvc.perform(put("/lines/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))

                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/lines/{id} 로 DELETE 요청을 보낼 시, 특정 노선을 삭제한다")
    void deleteLine() throws Exception {
        // given
        doNothing().when(linePropertyService).deleteLinePropertyById(any());

        // when
        mockMvc.perform(delete("/lines/{id}", 1L))

                // then
                .andExpect(status().isNoContent());
    }
}
