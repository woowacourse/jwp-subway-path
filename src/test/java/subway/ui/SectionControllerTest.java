package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import subway.application.SectionService;
import subway.application.dto.section.SectionCreateDto;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.section.SectionCreateRequest;
import subway.ui.dto.section.SectionDeleteRequest;

@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SectionService sectionService;

    @Test
    @DisplayName("/lines/{lineId}/sections 로 POST 요청과 함께 station의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void addSection_success() throws Exception {
        // given
        Long lineId = 1L;
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);
        given(sectionService.saveSection(any(SectionCreateDto.class))).willReturn(
                new Section(1L, new Station("잠실역"), new Station("잠실나루역"), Distance.from(10)));

        // expect
        mockMvc.perform(post("/lines/{lineId}/sections", lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startStationName").value("잠실역"))
                .andExpect(jsonPath("$.endStationName").value("잠실나루역"))
                .andExpect(jsonPath("$.distance").value(10));
    }

    @Test
    @DisplayName("/lines/{lineId}/sections 로 DELETE 요청과 함께 station의 정보를 보내면, HTTP 204 코드와 응답이 반환 되어야 한다.")
    void deleteSection_success() throws Exception {
        // given
        Long lineId = 1L;
        SectionDeleteRequest request = new SectionDeleteRequest("잠실역");

        // expect
        mockMvc.perform(delete("/lines/{lineId}/sections", lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
