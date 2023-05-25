package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import subway.service.StationService;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;
import subway.service.dto.StationRegisterResponse;

@RestController
public class StationController {

  private final StationService stationService;

  public StationController(final StationService stationService) {
    this.stationService = stationService;
  }

  @PostMapping("/stations")
  @ResponseStatus(HttpStatus.CREATED)
  public StationRegisterResponse registerStation(
      @RequestBody StationRegisterRequest stationRegisterRequest
  ) {
    stationService.registerStation(stationRegisterRequest);

    return new StationRegisterResponse(
        stationRegisterRequest.getLineName(),
        stationRegisterRequest.getNextStationName()
    );
  }

  @DeleteMapping("/stations")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteStation(@RequestBody StationDeleteRequest stationDeleteRequest) {
    stationService.deleteStation(stationDeleteRequest);
  }
}
