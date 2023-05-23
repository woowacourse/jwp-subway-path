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
import subway.controller.LineController;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.StationRegisterRequest;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.service.LineService;
import subway.service.SectionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        StationRegisterRequest section = new StationRegisterRequest(2L, 1L, 5);
        when(lineService.addStation(any(), any())).thenReturn(List.of(new SectionEntity(1L, 1L, 1L, 2L, 5)));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/line/{lineId}", 1L)
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
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/line/{lineId}/station/{stationId}", 1L, 2L))
                .andExpect(status().isNoContent())
                .andDo(document("line-delete-station",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void findLineById() throws Exception {
        Line line = new Line(1L, "2호선", "초록색", List.of(
                new StationEntity(1L, "신림역"),
                new StationEntity(2L, "봉천역"),
                new StationEntity(2L, "서울대입구역")
        ));

        when(lineService.findLineById(anyLong())).thenReturn(line);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/line/{lineId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("path-get-line",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void findAllLine() throws Exception {
        List<Line> line = List.of(new Line(1L, "2호선", "초록색", List.of(
                        new StationEntity(1L, "신림역"),
                        new StationEntity(2L, "봉천역"),
                        new StationEntity(2L, "서울대입구역")
                )),
                new Line(1L, "2호선", "초록색", List.of(
                        new StationEntity(1L, "신림역"),
                        new StationEntity(2L, "봉천역"),
                        new StationEntity(2L, "서울대입구역")
                )));

        when(lineService.findAllLine()).thenReturn(line);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/line"))
                .andExpect(status().isOk())
                .andDo(document("path-get-all-line",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
