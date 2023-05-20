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
import subway.domain.line.controller.LineController;
import subway.domain.line.dto.LineRequest;
import subway.domain.line.dto.SectionCreateRequest;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.entity.SectionEntity;
import subway.domain.line.service.LineService;
import subway.domain.line.service.SectionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(LineController.class)
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @MockBean
    private SectionService sectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        final LineEntity line = new LineEntity(1L, "2호선", "초록색");
        final LineRequest lineRequest = new LineRequest("2호선", "초록색");
        when(lineService.saveLine(any())).thenReturn(line);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/line")
                        .content(objectMapper.writeValueAsString(lineRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("line-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void update() throws Exception {
        final LineRequest lineRequest = new LineRequest("3호선", "파란색");

        mockMvc.perform(RestDocumentationRequestBuilders.put("/line/{lineId}", 1L)
                        .content(objectMapper.writeValueAsString(lineRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("line-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/line/{lineId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(document("line-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void addStation() throws Exception {
        SectionCreateRequest section = new SectionCreateRequest(1L, 1L, 2L, true, 5);

        when(sectionService.createSection(any())).thenReturn(List.of(new SectionEntity(1L, 1L, 1L, 2L, 5)));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/line/{lineId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(section)))
                .andExpect(status().isCreated())
                .andDo(document("line-add-station",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void deleteStation() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/line/{lineId}/station/{stationId}",1L,2L))
                .andExpect(status().isNoContent())
                .andDo(document("line-delete-station",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
