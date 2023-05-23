package subway.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.LineService;
import subway.ui.dto.LineRequest;
import subway.ui.dto.PostLineResponse;

@WebMvcTest(LineController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LineControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    LineService lineService;

    @Test
    void 추가금_포함_노선_등록_테스트() throws Exception {
        // given
        LineRequest request = new LineRequest("8호선", "pink", 1L, 2L, 10, 900);
        String jsonRequest = objectMapper.writeValueAsString(request);
        PostLineResponse response = new PostLineResponse(1L, "8호선", "pink", 900);
        given(lineService.saveLine(any(LineRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("lineId").value(1L))
            .andExpect(jsonPath("lineName").value("8호선"))
            .andExpect(jsonPath("lineColor").value("pink"))
            .andExpect(jsonPath("additionalCharge").value(900));
    }
}
