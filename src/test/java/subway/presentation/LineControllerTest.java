package subway.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.LineService;
import subway.exception.DuplicatedLineNameException;
import subway.presentation.dto.request.LineRequest;

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

    @Nested
    @DisplayName("노선 추가 - POST /lines")
    class Create {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final LineRequest requestDto = new LineRequest("신분당선", "bg-red-600", 10, "강남", "신논현");
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            given(lineService.save(any(), any())).willReturn(1L);

            // when, then
            mockMvc.perform(post("/lines")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location", "/lines/1"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("실패 - 중복된 노선 이름")
        void fail_duplicated_name() throws Exception {
            // given
            final LineRequest requestDto = new LineRequest("신분당선", "bg-red-600", 10, "강남", "신논현");
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            given(lineService.save(any(), any())).willThrow(DuplicatedLineNameException.class);

            // when, then
            mockMvc.perform(post("/lines")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest());
        }
    }


}
