package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import subway.application.SectionService;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;

@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SectionService sectionService;

    @Test
    @DisplayName("/sections/{lineId}로 POST 요청과 함께 station의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void addSection_success() throws Exception {
        // given
        Long lineId = 1L;
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);
        willDoNothing().given(sectionService).saveSection(any(SectionCreateRequest.class), anyLong());

        // expect
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/sections/{lineId}로 DELETE 요청과 함께 station의 정보를 보내면, HTTP 204 코드와 응답이 반환 되어야 한다.")
    void deleteSection_success() throws Exception {
        // given
        Long lineId = 1L;
        SectionDeleteRequest request = new SectionDeleteRequest("잠실역");

        // expect
        mockMvc.perform(delete("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
