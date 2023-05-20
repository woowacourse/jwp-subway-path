package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import subway.dto.request.SectionRequest;
import subway.service.SectionService;

@WebMvcTest(SectionController.class)
@DisplayName("SectionController 테스트")
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SectionService sectionService;

    @Test
    @DisplayName("createSection 요청 메세지 검증 기능 테스트")
    void validateCreateSectionDtoProperties() throws Exception {
        SectionRequest request = new SectionRequest(null, null, null, null);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/lines/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", containsString("must not be null")))
                .andExpect(jsonPath("$[1].message", containsString("must not be null")))
                .andExpect(jsonPath("$[2].message", containsString("must not be null")))
                .andExpect(jsonPath("$[3].message", containsString("must not be null")));
    }

    @Test
    @DisplayName("createSection 요청 메세지 Distance 검증 기능 테스트")
    void validateCreateSectionDtoDistancePositive() throws Exception {
        SectionRequest request = new SectionRequest(1L, 1L, 1L, -10);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/lines/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", containsString("must be greater than 0")));
    }
}
