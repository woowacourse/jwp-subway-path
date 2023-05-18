package subway.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.SectionService;
import subway.ui.dto.DeleteSectionRequest;
import subway.ui.dto.PostSectionRequest;
import subway.ui.dto.SectionResponse;

@WebMvcTest(SectionController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SectionService sectionService;

    @Test
    void section_추가_테스트() throws Exception {
        // given
        PostSectionRequest request = new PostSectionRequest(1L, 2L, 10);
        String jsonRequest = new ObjectMapper().writeValueAsString(request);
        SectionResponse response = new SectionResponse(1L);
        given(sectionService.saveSection(any(Long.class), any(PostSectionRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/lines/" + 1L + "/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/sections/1"));
    }

    @Test
    void section_삭제_테스트() throws Exception {
        // given
        DeleteSectionRequest request = new DeleteSectionRequest(1L);
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        // when & then
        mockMvc.perform(patch("/lines/" + 1L + "/unregister")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isNoContent());
    }
}
