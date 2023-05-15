package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.service.LineService;
import subway.controller.dto.request.LineRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    @DisplayName("초기 노선 생성 성공 - POST /lines/initial")
    void create_initial_success() throws Exception {
        // given
        final LineRequest requestDto = new LineRequest("매튜당선", "bg-chicken-600", 10, "매", "튜");
        final String requestBody = objectMapper.writeValueAsString(requestDto);
        given(lineService.save(any(), any()))
                .willReturn(1L);

        // expect
        mockMvc.perform(post("/lines")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", "/lines/1"))
                .andExpect(status().isCreated());
    }
}
