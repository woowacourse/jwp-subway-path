package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.line.facade.LineFacade;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineResponse;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LineEntityControllerTest {

    @MockBean
    private LineFacade lineFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveRequest를_받아_호선을_생성한다() throws Exception {
        // given
        final LineRequest saveRequest = new LineRequest("2호선", "초록", 10);
        final String request = objectMapper.writeValueAsString(saveRequest);

        // when, then
        mockMvc.perform(post("/lines/" + 1L + "/" + 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    void 모든_호선을_조회한다() throws Exception {
        // given
        final List<LineResponse> responses = List.of(
                new LineResponse(1L, "1호선", "파랑"),
                new LineResponse(2L, "2호선", "초록")
        );
        when(lineFacade.getAll()).thenReturn(responses);
        final String responseJson = objectMapper.writeValueAsString(responses);

        // when, then
        mockMvc.perform(get("/lines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());
    }

    @Test
    void id를_받아_해당_호선을_조회한다() throws Exception {
        // given
        final Long id = 1L;
        final LineResponse lineResponse = new LineResponse(id, "1호선", "파랑");

        when(lineFacade.getLineResponseById(id)).thenReturn(lineResponse);
        final String responseJson = objectMapper.writeValueAsString(lineResponse);

        // when, then
        mockMvc.perform(get("/lines/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());
    }

    @Test
    void id와_saveRequest를_받아_해당_호선을_수정한다() throws Exception {
        // given
        final Long id = 1L;
        final LineRequest request = new LineRequest("2호선", "검정", 10);
        final String requestJson = objectMapper.writeValueAsString(request);

        // when, then
        mockMvc.perform(put("/lines/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print());

        verify(lineFacade, times(1)).updateLine(id, request);
    }

    @Test
    void id를_받아_해당_호선을_삭제한다() throws Exception {
        // given
        final Long id = 1L;
        final LineRequest request = new LineRequest("1호선", "파랑", 10);
        lineFacade.createLine(request, 1L, 2L);

        // when, then
        mockMvc.perform(delete("/lines/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(lineFacade, times(1)).deleteLineById(id);
    }
}
