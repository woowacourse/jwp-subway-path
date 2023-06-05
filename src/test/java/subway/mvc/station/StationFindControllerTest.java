package subway.mvc.station;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.mvc.AbstractControllerTest;
import subway.station.ui.StationAssembler;
import subway.station.ui.dto.in.StationInfoResponse;
import subway.station.ui.dto.in.StationInfosResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 전체 조회 mvc 테스트")
class StationFindControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_조회된다() throws Exception {
        // given
        final List<StationInfoResponseDto> input = List.of(new StationInfoResponseDto(1L, "name1"),
            new StationInfoResponseDto(2L, "name2"));
        given(stationFindAllUseCase.findAll())
            .willReturn(input);
        final List<StationInfoResponse> resultList = input.stream().map(StationAssembler::toStationInfoResponse)
            .collect(Collectors.toList());
        final String result = objectMapper.writeValueAsString(new StationInfosResponse(resultList));

        // when
        mockMvc.perform(get("/stations"))

            // then
            .andExpect(status().isOk())
            .andExpect(content().json(result));
    }
}
