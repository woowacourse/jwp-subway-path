package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.strategy.DeleteSectionStrategy;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.InvalidInputException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final LineRepository lineRepository;

    public StationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Optional<Long> findStation = lineRepository.findStationByName(stationRequest.getName());

        if(findStation.isPresent()){
            throw new InvalidInputException("이미 존재하는 역입니다.");
        }

        Station station = lineRepository.saveStation(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(lineRepository.findStationById(id));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> allStations = lineRepository.findAllStations();

        return allStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        lineRepository.updateStation(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        List<Line> allLines = lineRepository.findAll();
        Station deletStation = lineRepository.findStationById(id);

        for (Line line : allLines) {
            try{
                DeleteSectionStrategy deleteSectionStrategy = line.readyToDelete(deletStation);
                deleteSectionStrategy.execute(lineRepository);
            } catch (InvalidInputException e){
                continue;
            }
        }
        lineRepository.removeStationById(deletStation.getId());
    }
}