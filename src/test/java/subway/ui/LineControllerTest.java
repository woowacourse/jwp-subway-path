package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.domain.Line;
import subway.ui.dto.line.LineCreateRequest;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("/lines로 POST 요청과 함께 line의 정보를 보내면, HTTP 201 코드와 응답이 반환되어야 한다.")
    void createLine_success() throws Exception {
        // given
        LineCreateRequest request = new LineCreateRequest("2호선", "bg-red-600");
        given(lineService.saveLine(any(LineCreateRequest.class))).willReturn(new Line(1L, "2호선", "green"));

        // expect
        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
