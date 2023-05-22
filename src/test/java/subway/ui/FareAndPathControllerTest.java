package subway.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static subway.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.FareAndPathService;
import subway.ui.dto.FareAndPathResponse;
import subway.ui.dto.FareAndPathRequest;

@WebMvcTest(FareAndPathController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FareAndPathControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FareAndPathService fareAndPathService;

    @Test
    void 단일_노선_path_조회_테스트() throws Exception {
        // given
        FareAndPathRequest request = new FareAndPathRequest(cheonho.getId(), jangji.getId(), 20);
        String jsonRequest = objectMapper.writeValueAsString(request);
        FareAndPathResponse response = FareAndPathResponse.from(1250, List.of(cheonho, jamsil, jangji));
        given(fareAndPathService.findFareAndPath(any(FareAndPathRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/path")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("fare").value(1250))
            .andExpect(jsonPath("$.stations[?(@.name=='천호')]").exists())
            .andExpect(jsonPath("$.stations[?(@.name=='잠실')]").exists())
            .andExpect(jsonPath("$.stations[?(@.name=='장지')]").exists());
    }
}
