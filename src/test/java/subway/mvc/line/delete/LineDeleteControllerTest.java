package subway.mvc.line.delete;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.mvc.AbstractControllerTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 삭제하는 기능 mvc 테스트")
class LineDeleteControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_삭제된다() throws Exception {
        mockMvc.perform(delete("/lines/1"))

                .andExpect(status().isNoContent());
    }
}
