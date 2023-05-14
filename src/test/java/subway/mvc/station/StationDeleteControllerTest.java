package subway.mvc.station;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.mvc.AbstractControllerTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 삭제하는 mvc 테스트")
class StationDeleteControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_삭제된다() throws Exception {
        mockMvc.perform(delete("/stations/1"))

            .andExpect(status().isNoContent());
    }
}
