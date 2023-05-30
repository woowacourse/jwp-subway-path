package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SubwayService;
import subway.dto.AddStationRequest;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static subway.utils.StationFixture.JAMSIL_STATION;
import static subway.utils.TestUtils.toJson;

@SuppressWarnings(value = "NonAsciiCharacters")
@WebMvcTest
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubwayService subwayService;

    @DisplayName("createStation은 AddStationRequest를 요청받으면 역을 노선에 추가하면 CREATED 응답코드를 반환한다.")
    @Test
    void createStation() throws Exception {
        AddStationRequest addStationRequest = new AddStationRequest("신림", 1L, 2L, 1L, 5);

        doReturn(1L).when(subwayService).addStation(addStationRequest);

        mockMvc.perform(post("/line/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(addStationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/line/stations/1"));
    }

    @DisplayName("getStation은 Station id에 해당하는 Station을 반환한다.")
    @Test
    void getStation() throws Exception {
        doReturn(JAMSIL_STATION).when(subwayService).findStationById(1);

        mockMvc.perform(get("/line/stations/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value("잠실"));
    }
}
