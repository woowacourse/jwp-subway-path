package subway.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@WebMvcTest(SectionController.class)
class SectionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private SectionService sectionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("구역 추가 테스트")
    void addSectionMvcTest() throws Exception {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, "UP", 1);
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 1L, 1);
        // when
        when(this.sectionService.insertSection(sectionRequest)).thenReturn(List.of(sectionResponse));
        // then
        this.mockMvc.perform(post("/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(sectionRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].lineId").value(1L))
                .andExpect(jsonPath("$[0].upStationId").value(2L))
                .andExpect(jsonPath("$[0].downStationId").value(1L))
                .andExpect(jsonPath("$[0].distance").value(1L));
    }
    
    @Test
    @DisplayName("구역 조회 테스트")
    void getSectionsMvcTest() throws Exception {
        // given
        final SectionResponse sectionResponse = new SectionResponse(1L, 1L, 2L, 1L, 1);
        // when
        when(this.sectionService.findSectionsByLineId(1L)).thenReturn(List.of(sectionResponse));
        // then
        this.mockMvc.perform(get("/lines/1/sections"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lineId").value(1L))
                .andExpect(jsonPath("$[0].upStationId").value(2L))
                .andExpect(jsonPath("$[0].downStationId").value(1L))
                .andExpect(jsonPath("$[0].distance").value(1L));
    }
    
}