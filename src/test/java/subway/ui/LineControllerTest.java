package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import subway.dto.response.LineWithStationResponse;
import subway.dto.response.StationResponse;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineController.class)
class LineControllerTest extends ControllerTest {

    @DisplayName("노선을 조회해서 역을 순서대로 보여준다.")
    @Test
    void findByLineId() throws Exception {
        //given
        final LineWithStationResponse lineWithStationResponse = new LineWithStationResponse(1L, "1호선", "파랑", List.of(
                new StationResponse(1L, "수원"),
                new StationResponse(2L, "성대")
        ));
        given(lineService.findLineById(anyLong()))
                .willReturn(lineWithStationResponse);

        //then
        mockMvc.perform(get("/lines/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stations[1].name", is("성대")));
    }


    @DisplayName("전체 노선을 조회한다.")
    @Test
    void findAllLinesTest() throws Exception {
        //given
        given(lineService.findAllLines())
                .willReturn(List.of(
                        new LineWithStationResponse(1L, "1호선", "파랑",
                                List.of(new StationResponse(1L, "강남"),
                                        new StationResponse(2L, "역삼"))),
                        new LineWithStationResponse(2L, "2호선", "파랑",
                                List.of(new StationResponse(1L, "강남"),
                                        new StationResponse(2L, "수원")
                                ))));

        //then
        mockMvc.perform(get("/lines"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].name", is("2호선")))
                .andExpect(jsonPath("$.[1].stations.[1].name", is("수원")));
    }
}
