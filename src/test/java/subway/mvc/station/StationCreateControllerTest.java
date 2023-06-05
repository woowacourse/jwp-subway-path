package subway.mvc.station;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.mvc.AbstractControllerTest;
import subway.station.ui.dto.in.StationCreateRequest;
import subway.station.ui.dto.in.StationInfoResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 생성하는 mvc 테스트")
class StationCreateControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_생성된다() throws Exception {
        // when
        final StationInfoResponseDto stationInfoResponseDto = new StationInfoResponseDto(1L, "강남역");
        given(stationCreateUseCase.create(any()))
            .willReturn(stationInfoResponseDto);
        final String requestBody = objectMapper.writeValueAsString(new StationCreateRequest("강남역"));
        final String expected = objectMapper.writeValueAsString(new StationInfoResponse(1L, "강남역"));

        // then
        mockMvc.perform(post("/stations")
                .contentType(APPLICATION_JSON)
                .content(requestBody))

            .andExpect(status().isCreated())
            .andExpect(content().json(expected))
            .andExpect(header().string("Location", "/stations/1"));
    }
}
