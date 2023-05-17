package subway.controller;

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
import subway.facade.StationFacade;
import subway.presentation.dto.StationResponse;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private StationFacade stationFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveRequest를_받아_역을_생성한다() throws Exception {
        // when, then
        mockMvc.perform(post("/stations")
                        .param("name", "잠실역"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    void 모든_역을_조회한다() throws Exception {
        // given
        final List<StationResponse> responses = List.of(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "선릉역")
        );
        when(stationFacade.getAllByLineId(1L)).thenReturn(responses);
        final String responseJson = objectMapper.writeValueAsString(responses);

        // when, then
        mockMvc.perform(get("/stations/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());
    }

    @Test
    void id와_saveRequest를_받아_해당_역을_수정한다() throws Exception {
        // given
        final Long id = 1L;
        stationFacade.createStation("잠실역");

        // when, then
        mockMvc.perform(put("/stations/" + id)
                        .param("name", "잠실역"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(stationFacade, times(1)).updateById(id, "잠실역");
    }

    @Test
    void id를_받아_해당_역을_삭제한다() throws Exception {
        // given
        final Long lineId = 1L;
        final Long stationId = 1L;
        stationFacade.createStation("잠실역");

        // when, then
        mockMvc.perform(delete("/stations/" + lineId + "/" + stationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(stationFacade, times(1)).deleteById(lineId, stationId);
    }
}
