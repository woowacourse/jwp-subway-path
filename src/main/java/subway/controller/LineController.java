package subway.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Line;
import subway.dto.AddStationToExistLineDto;
import subway.dto.CreateNewLineDto;
import subway.dto.request.AddStationToLineRequest;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.AddStationToLineResponse;
import subway.dto.response.DeleteStationFromLineResponse;
import subway.dto.response.GetAllStationsInLineResponse;
import subway.dto.response.LineCreateResponse;
import subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineCreateResponse> createLine(@RequestBody LineCreateRequest request) {
        CreateNewLineDto dto = new CreateNewLineDto(
                request.getLineName(),
                request.getExtraCharge(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
        Line createdLine = lineService.createNewLine(dto);

        LineCreateResponse response = LineCreateResponse.fromDomain(createdLine);
        return ResponseEntity.created(URI.create("/lines/" + createdLine.getId())).body(response);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<AddStationToLineResponse> addStationToLine(@PathVariable Long lineId,
                                                                     @RequestBody AddStationToLineRequest request) {
        AddStationToExistLineDto dto = new AddStationToExistLineDto(
                lineId,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
        Line updatedLine = lineService.addStationToExistLine(dto);

        AddStationToLineResponse response = AddStationToLineResponse.fromDomain(updatedLine);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<DeleteStationFromLineResponse> deleteStationFromLine(@PathVariable Long lineId,
                                                                               @PathVariable Long stationId) {
        Line updatedLine = lineService.deleteStationFromLine(lineId, stationId);

        DeleteStationFromLineResponse response = DeleteStationFromLineResponse.fromDomain(updatedLine);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<GetAllStationsInLineResponse> findLine(@PathVariable Long lineId) {
        Line findLine = lineService.findOneLine(lineId);

        GetAllStationsInLineResponse response = GetAllStationsInLineResponse.fromDomain(findLine);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GetAllStationsInLineResponse>> findAllLines() {
        List<GetAllStationsInLineResponse> response = lineService.findAllLine()
                .stream().map(GetAllStationsInLineResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
