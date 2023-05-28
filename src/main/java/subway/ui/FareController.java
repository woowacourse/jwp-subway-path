package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.GeneralFareService;
import subway.application.request.UpdateLineExpenseRequest;
import subway.application.response.LineExpenseResponse;

import javax.validation.Valid;

@RestController
public class FareController {

    private final GeneralFareService generalFareService;

    public FareController(final GeneralFareService generalFareService) {
        this.generalFareService = generalFareService;
    }

    @PatchMapping("/lines/{lineId}/expense")
    public ResponseEntity<Long> updateLineExpense(
            @PathVariable Long lineId,
            @Valid @RequestBody UpdateLineExpenseRequest request
    ) {
        final Long updateLineExpenseId = generalFareService.updateLineExpense(lineId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateLineExpenseId);
    }

    @GetMapping("/lines/{lineId}/expense")
    public ResponseEntity<LineExpenseResponse> findLineExpenseByLineId(@PathVariable("lineId") Long lineId) {
        final LineExpenseResponse findLineExpenseResponse = generalFareService.findLineExpenseByLineId(lineId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findLineExpenseResponse);
    }
}
