package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.StationService;
import subway.dto.StationResponse;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest()
class StationEntityControllerTest {

    @MockBean
    private StationService stationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void saveRequest를_받아_역을_생성한다() throws Exception {
//        // given
//        final StationSaveRequest saveRequest = new StationSaveRequest("잠실역", "강남역");
//        final String request = objectMapper.writeValueAsString(saveRequest);
//
//        // when, then
//        mockMvc.perform(post("/stations")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andReturn();
//    }

    @Test
    void 모든_역을_조회한다() throws Exception {
        // given
        final List<StationResponse> responses = List.of(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "선릉역")
        );
        when(stationService.getAllStationResponses(1L)).thenReturn(responses);
        final String responseJson = objectMapper.writeValueAsString(responses);

        // when, then
        mockMvc.perform(get("/stations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());
    }

//    @Test
//    void id와_saveRequest를_받아_해당_역을_수정한다() throws Exception {
//        // given
//        final Long id = 1L;
//        final StationSaveRequest station = new StationSaveRequest("잠실역", "강남역");
//        final String requestJson = objectMapper.writeValueAsString(station);
//        stationService.saveStation(station);
//
//        // when, then
//        mockMvc.perform(put("/stations/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        verify(stationService, times(1)).updateStation(id, station);
//    }

//    @Test
//    void id를_받아_해당_역을_삭제한다() throws Exception {
//        // given
//        final Long id = 1L;
//        final StationSaveRequest station = new StationSaveRequest("잠실역", "강남역");
//        stationService.saveStation(station);
//
//        // when, then
//        mockMvc.perform(delete("/stations/" + id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        verify(stationService, times(1)).deleteStationById(id);
//    }
}
