package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.fixture.Fixture.station1;
import static subway.fixture.Fixture.station2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.domain.transfer.Transfer;
import subway.business.service.TransferService;
import subway.business.service.dto.TransferRequest;
import subway.business.service.dto.TransferResponse;

@WebMvcTest(TransferController.class)
class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService transferService;

    @DisplayName("환승역을 등록한다.")
    @Test
    void shouldCreateTransferWhenRequest() throws Exception {
        Transfer transfer = new Transfer(1L, station1(), station2());
        given(transferService.createTransfer(any())).willReturn(TransferResponse.from(transfer));

        TransferRequest transferRequest = new TransferRequest(1L, 2L);
        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstStation.name").value("잠실역"))
                .andExpect(jsonPath("$.lastStation.name").value("몽촌토성역"))
                .andExpect(status().isOk());
    }
}
