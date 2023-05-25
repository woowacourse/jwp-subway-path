package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.fixture.FixtureForLineTest.station1;
import static subway.fixture.FixtureForLineTest.station2;
import static subway.fixture.FixtureForLineTest.station3;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

    @DisplayName("모든 환승역을 조회한다.")
    @Test
    void shouldReadAllTransferWhenRequest() throws Exception {
        Transfer transfer1 = new Transfer(1L, station1(), station2());
        Transfer transfer2 = new Transfer(2L, station2(), station3());
        given(transferService.findAllTransfers()).willReturn(List.of(
                        TransferResponse.from(transfer1),
                        TransferResponse.from(transfer2)
                )
        );

        mockMvc.perform(get("/transfers"))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].firstStation.name").value("잠실역"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].lastStation.name").value("까치산역"));
    }
}
