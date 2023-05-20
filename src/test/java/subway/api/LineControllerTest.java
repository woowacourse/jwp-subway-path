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
import subway.domain.line.entity.LineEntity;
import subway.domain.line.service.LineService;

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
}
