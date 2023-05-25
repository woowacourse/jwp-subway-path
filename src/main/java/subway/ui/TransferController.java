package subway.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.business.service.TransferService;
import subway.business.service.dto.TransferRequest;
import subway.business.service.dto.TransferResponse;

@Tag(name = "Transfer", description = "환승 API Document")
@RequestMapping("/transfers")
@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(summary = "환승 추가", description = "두 개의 역을 환승역으로 만듭니다.")
    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@RequestBody TransferRequest transferRequest) {
        TransferResponse transferResponse = transferService.createTransfer(transferRequest);
        return ResponseEntity.ok().body(transferResponse);
    }

    @Operation(summary = "모든 환승역 조회", description = "모든 환승역의 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<TransferResponse>> findAllTransfers() {
        List<TransferResponse> transferResponses = transferService.findAllTransfers();
        return ResponseEntity.ok().body(transferResponses);
    }
}
