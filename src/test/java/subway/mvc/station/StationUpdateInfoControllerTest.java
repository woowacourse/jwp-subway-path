package subway.mvc.station;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.mvc.AbstractControllerTest;
import subway.station.ui.dto.in.StationUpdateInfoRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역 정보 수정 mvc 테스트")
class StationUpdateInfoControllerTest extends AbstractControllerTest {

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
    void 변경에서_본문이_잘못_들어오면_400_에러를_반환한다(final String input) throws Exception {
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
