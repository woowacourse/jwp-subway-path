package subway.presentation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.business.StationService;
import subway.exception.StationNotFoundException;
import subway.presentation.dto.request.StationRequest;
import subway.presentation.dto.response.ExceptionResponse;
import subway.presentation.dto.response.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> create(@RequestBody final StationRequest stationRequest) {
        final StationResponse response = stationService.save(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> readAll() {
        return ResponseEntity.ok().body(stationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> read(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable final Long id, @RequestBody final StationRequest stationRequest) {
        stationService.update(id, stationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        stationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyResultDataAccessException() {
        return ResponseEntity.badRequest().body(new ExceptionResponse("존재하지 않는 역입니다."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException() {
        return ResponseEntity.badRequest().body(new ExceptionResponse("중복된 역이름입니다."));
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleStationNotFoundException(StationNotFoundException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

}
