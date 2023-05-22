package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LineService lineService;

    private LineRequest request1;
    private LineRequest request2;
    private LineRequest request3;
    private LineResponse response1;
    private LineResponse response2;
    private LineResponse response3;

    @BeforeEach
    void setUp() {
        request1 = new LineRequest("1호선", "blue");
        request2 = new LineRequest("2호선", "green");
        request3 = new LineRequest("3호선", "orange");
        response1 = new LineResponse(1L, "1호선", "blue");
        response2 = new LineResponse(2L, "2호선", "green");
        response3 = new LineResponse(3L, "3호선", "orange");
    }

    @Test
    @DisplayName("post /lines : created를 반환하고 Location에 uri를 저장한다")
    void createLine() throws Exception {
        // given
        final String jsonRequest = objectMapper.writeValueAsString(request1);
        when(lineService.createLine(any())).thenReturn(response1);

        // when & then
        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/1"));

        verify(lineService, times(1)).createLine(refEq(request1));
    }

    @Test
    @DisplayName("get /lines : ok를 반환하고 모든 LineResponse를 반환한다")
    void findAllLines() throws Exception {
        // given
        final List<LineResponse> responses = List.of(response1, response2, response3);
        final String jsonResponses = objectMapper.writeValueAsString(responses);
        when(lineService.findLineResponses()).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/lines")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponses));

        verify(lineService, times(1)).findLineResponses();
    }

    @Test
    @DisplayName("get /lines/{id} : ok를 반환하고 id에 해당하는 stationResponse를 반환한다")
    void findLineById() throws Exception {
        // given
        when(lineService.findLineResponseById(any())).thenReturn(response1);
        final String jsonResponse = objectMapper.writeValueAsString(response1);

        // when & then
        mockMvc.perform(get("/lines/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        verify(lineService, times(1)).findLineResponseById(1L);
    }

    @Test
    @DisplayName("put /lines/{id} : ok를 반환한다")
    void updateLine() throws Exception {
        // given
        final String jsonRequest = objectMapper.writeValueAsString(request1);

        // when & then
        mockMvc.perform(put("/lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(lineService, times(1)).updateLine(any(), any());
    }

    @Test
    @DisplayName("delete /stations/{id} : noContent를 반환한다")
    void deleteLine() throws Exception {
        mockMvc.perform(delete("/lines/1"))
                .andExpect(status().isNoContent());

        verify(lineService, times(1)).deleteLineById(1L);
    }
}