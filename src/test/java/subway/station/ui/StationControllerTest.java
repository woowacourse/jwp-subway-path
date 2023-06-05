package subway.station.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.webmvc.AbstractControllerTest;
import subway.station.application.dto.request.StationInfoResponseDto;
import subway.station.ui.dto.reqest.StationCreateRequest;
import subway.station.ui.dto.reqest.StationUpdateInfoRequest;
import subway.station.ui.dto.response.StationInfoResponse;
import subway.station.ui.dto.response.StationInfosResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StationControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_생성된다() throws Exception {
        // when
        StationInfoResponseDto stationInfoResponseDto = new StationInfoResponseDto(1L, "강남역");
        given(stationCommandService.create(any()))
                .willReturn(stationInfoResponseDto);
        String requestBody = objectMapper.writeValueAsString(new StationCreateRequest("강남역"));
        String expected = objectMapper.writeValueAsString(new StationInfoResponse(1L, "강남역"));

        // then
        mockMvc.perform(post("/stations")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isCreated())
                .andExpect(content().json(expected))
                .andExpect(header().string("Location", "/stations/1"));
    }

    @Test
    void 정상적으로_삭제된다() throws Exception {
        mockMvc.perform(delete("/stations/1"))

                .andExpect(status().isNoContent());
    }

    @Test
    void 정상적으로_조회된다() throws Exception {
        given(stationQueryService.findStationInfoById(1))
                .willReturn(new StationInfoResponseDto(1, "name"));
        String expected = objectMapper.writeValueAsString(new StationInfoResponse(1, "name"));

        mockMvc.perform(get("/stations/1"))

                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void 정상적으로_전체가_조회된다() throws Exception {
        // given
        List<StationInfoResponseDto> input = List.of(new StationInfoResponseDto(1L, "name1"),
                new StationInfoResponseDto(2L, "name2"));
        given(stationQueryService.findAll())
                .willReturn(input);
        List<StationInfoResponse> resultList = input.stream().map(StationAssembler::toStationInfoResponse)
                .collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(new StationInfosResponse(resultList));

        // when
        mockMvc.perform(get("/stations"))

                // then
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void 변경을_정상적으로_수행한다() throws Exception {
        mockMvc.perform(put("/stations/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationUpdateInfoRequest("강남역"))))
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest(name = "입력값 : {0}")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void 변경에서_본문이_잘못_들어오면_400_에러를_반환한다(String input) throws Exception {
        mockMvc.perform(put("/stations/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationUpdateInfoRequest(input))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 변경에서_pathVariable_이_잘못되면_400_예외가_발생한다() throws Exception {
        final String input = "asdf";

        mockMvc.perform(put("/stations/" + input)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationUpdateInfoRequest("강남역"))))

                .andExpect(status().isBadRequest());
    }
}
