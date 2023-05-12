package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.dto.LineWithStationResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @DisplayName("노선을 조회해서 역을 순서대로 보여준다.")
    @Test
    void findByLineId() throws Exception {
        //given
        final LineWithStationResponse lineWithStationResponse = new LineWithStationResponse(1L, "1호선", "파랑", List.of(
                new StationResponse(1L, "수원"),
                new StationResponse(2L, "성대")
        ));
        given(lineService.findLineWithStation(anyLong()))
                .willReturn(lineWithStationResponse);

        //then
        mockMvc.perform(get("/lines/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stations[1].name", is("성대")));
    }
}
