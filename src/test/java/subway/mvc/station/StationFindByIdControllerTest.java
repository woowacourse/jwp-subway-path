package subway.mvc.station;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.mvc.AbstractControllerTest;
import subway.station.ui.dto.in.StationInfoResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 id를 통해 조회 mvc 테스트")
class StationFindByIdControllerTest extends AbstractControllerTest {


    @Test
    void 정상적으로_조회된다() throws Exception {
        given(stationFindByIdUseCase.findStationInfoById(1))
            .willReturn(new StationInfoResponseDto(1, "name"));
        final String expected = objectMapper.writeValueAsString(new StationInfoResponse(1, "name"));

        mockMvc.perform(get("/stations/1"))

            .andExpect(status().isOk())
            .andExpect(content().json(expected));
    }
}
