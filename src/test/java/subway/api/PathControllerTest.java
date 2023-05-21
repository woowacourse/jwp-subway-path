package subway.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import subway.domain.line.controller.PathController;
import subway.domain.line.domain.Path;
import subway.domain.line.service.PathService;
import subway.domain.line.entity.StationEntity;

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
