package subway.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import subway.domain.section.controller.SectionController;
import subway.domain.section.dto.SectionCreateRequest;
import subway.domain.section.dto.SectionDeleteRequest;
import subway.domain.section.entity.SectionEntity;
import subway.domain.section.service.SectionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(SectionController.class)
public class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SectionService sectionService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        SectionCreateRequest section = new SectionCreateRequest(1L, 1L, 2L, true, 5);

        when(sectionService.createSection(any())).thenReturn(List.of(new SectionEntity(1L, 1L, 1L, 2L, 5)));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(section)))
                .andExpect(status().isCreated())
                .andDo(document("section-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void delete() throws Exception {
        SectionDeleteRequest section = new SectionDeleteRequest(1L, 2L);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(section)))
                .andExpect(status().isNoContent())
                .andDo(document("section-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
