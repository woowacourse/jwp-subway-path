package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.dto.LineCreateRequest;
import subway.service.LineService;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @Nested
    @DisplayName("노선 생성 요청시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 노선 정보라면 새로운 노선을 추가한다.")
        void createLine() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색");

            given(lineService.createLine(any(LineCreateRequest.class))).willReturn(1L);

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, containsString("/lines/1")));
        }

        @Test
        @DisplayName("이름이 공백이라면 400 상태를 반환한다.")
        void createLineWithInvalidName() throws Exception {
            final LineCreateRequest request = new LineCreateRequest(" ", "초록색");

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 이름은 공백일 수 없습니다."));
        }

        @Test
        @DisplayName("색이 공백이라면 400 상태를 반환한다.")
        void createLineWithInvalidColor() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", " ");

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 색깔은 공백일 수 없습니다."));
        }
    }
}
