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
import subway.controller.StationController;
import subway.dto.StationCreateRequest;
import subway.entity.StationEntity;
import subway.service.StationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest(StationController.class)
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StationService stationService;
    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void findById() throws Exception {
        StationEntity station = new StationEntity(1L, "봉천역");
        when(stationService.findStationById(anyLong())).thenReturn(station);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/stations/{stationId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("station-get-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void findAll() throws Exception {
        List<StationEntity> stations = List.of(
                new StationEntity(1L, "신림역"),
                new StationEntity(2L, "봉천역"),
                new StationEntity(3L, "서울대입구역")
        );

        when(stationService.findAllStation()).thenReturn(stations);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/stations"))
                .andExpect(status().isOk())
                .andDo(document("station-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void create() throws Exception {
        StationCreateRequest request = new StationCreateRequest("봉천역");
        StationEntity station = new StationEntity(1L, "봉천역");
        when(stationService.saveStation(any())).thenReturn(station);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andDo(document("station-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void update() throws Exception {
        StationCreateRequest station = new StationCreateRequest("봉천역");
        mockMvc.perform(RestDocumentationRequestBuilders.put("/stations/{stationId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isOk())
                .andDo(document("station-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/stations/{stationId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("station-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stationId").description("역 id")
                        )
                ));
    }
}
