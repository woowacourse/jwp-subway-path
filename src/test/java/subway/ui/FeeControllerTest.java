package subway.ui;

import org.junit.jupiter.api.Test;
import subway.dto.response.ShortestPathResponse;
import subway.dto.response.StationResponse;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeeControllerTest extends ControllerTest {

    @Test
    void 요금을_조회한다() throws Exception {
        given(feeService.showShortestPath(anyLong(), anyLong()))
                .willReturn(new ShortestPathResponse(1250, List.of(
                        new StationResponse(1L, "1번역"),
                        new StationResponse(2L, "2번역")
                )));

        mockMvc.perform(get("/fee?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fee", is(1250)));
    }
}
