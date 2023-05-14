package subway.mvc.line;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.mvc.AbstractControllerTest;
import subway.ui.line.dto.in.LineUpdateInfoRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 수정하는 기능 mvc 테스트")
class LineUpdateControllerTest extends AbstractControllerTest {

    @Test
    void 정상적으로_수정된다() throws Exception {
        final String request = objectMapper.writeValueAsString(new LineUpdateInfoRequest("2호선", "초록색"));

        mockMvc.perform(patch("/lines/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))

            .andExpect(status().isNoContent());
    }
}
