package subway.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import subway.domain.line.entity.LineEntity;
import subway.domain.path.controller.PathController;
import subway.domain.path.domain.LinePath;
import subway.domain.path.domain.Path;
import subway.domain.path.service.PathService;
import subway.domain.station.entity.StationEntity;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(PathController.class)
public class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @Test
    void findLineById() throws Exception {
        LinePath linePath = new LinePath(new LineEntity(1L, "2호선", "초록색"), List.of(
                new StationEntity(1L, "신림역"),
                new StationEntity(2L, "봉천역"),
                new StationEntity(2L, "서울대입구역")
        ));

        when(pathService.findById(anyLong())).thenReturn(linePath);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/path/line/{lineId}",1L))
                .andExpect(status().isOk())
                .andDo(document("path-get-line",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void findAllLine() throws Exception {
        List<LinePath> linePaths = List.of(
                new LinePath(new LineEntity(1L, "2호선", "초록색"), List.of(
                        new StationEntity(1L, "신림역"),
                        new StationEntity(2L, "봉천역"),
                        new StationEntity(2L, "서울대입구역")
                )),
                new LinePath(new LineEntity(1L, "1호선", "파란색"), List.of(
                        new StationEntity(1L, "시청역"),
                        new StationEntity(2L, "서울역"),
                        new StationEntity(2L, "남영역")
                ))
        );

        when(pathService.findAll()).thenReturn(linePaths);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/path/lines"))
                .andExpect(status().isOk())
                .andDo(document("path-get-all-line",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void findPath() throws Exception {
        when(pathService.findShortestPath(anyLong(),anyLong())).thenReturn(new Path(
                List.of(
                        new StationEntity(1L,"신림역"),
                        new StationEntity(2L,"봉천역"),
                        new StationEntity(3L,"서울대입구역")

                )
                ,15
        ));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/path")
                        .queryParam("startLineId", "1")
                        .queryParam("endLineId", "3")
                )
                .andExpect(status().isOk())
                .andDo(document("path-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
