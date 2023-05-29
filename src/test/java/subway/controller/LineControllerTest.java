package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.service.LineService;
import subway.service.SectionService;

@WebMvcTest(LineController.class)
@DisplayName("LineController 테스트")
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LineService lineService;
    @MockBean
    private SectionService sectionService;

    @Test
    @DisplayName("createLine 요청 메세지 검증 기능 테스트")
    void validateCreateLineDtoProperties() throws Exception {
        LineRequest request = new LineRequest(null, null, null);
        LineResponse response = new LineResponse(1L, null, null);
        ObjectMapper objectMapper = new ObjectMapper();

        when(lineService.saveLine(any())).thenReturn(response);

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", containsString("must not be blank")))
                .andExpect(jsonPath("$[1].message", containsString("must not be blank")));
    }

    @Test
    @DisplayName("updateLine 요청 메세지 검증 기능 테스트")
    void validateUpdateLineDtoProperties() throws Exception {
        LineRequest request = new LineRequest(null, null, null);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", containsString("must not be blank")))
                .andExpect(jsonPath("$[1].message", containsString("must not be blank")));
    }
}
