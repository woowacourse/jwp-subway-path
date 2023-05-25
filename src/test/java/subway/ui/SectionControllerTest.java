package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SectionService;
import subway.dto.request.SectionRequest;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SectionService sectionService;

    @DisplayName("구간을 추가할 수 있다")
    @Test
    void createSection() throws Exception {
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 7);
        willDoNothing().given(sectionService)
                       .saveSection(anyLong(), any());

        mockMvc.perform(post("/sections/1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(sectionRequest)))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", "/lines/1"))
               .andDo(print());
    }

    @DisplayName("구간을 삭제할 수 있다")
    @Test
    void deleteSection() throws Exception {
        Long stationId = 1L;
        willDoNothing().given(sectionService)
                       .removeStationFromLine(anyLong(), anyLong());

        mockMvc.perform(delete("/sections/1")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(objectMapper.writeValueAsString(stationId)))
               .andExpect(status().isNoContent())
               .andDo(print());
    }
}
