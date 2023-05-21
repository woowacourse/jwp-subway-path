package subway.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.general.GeneralSections;
import subway.domain.section.general.NewGeneralSectionMaker;
import subway.domain.station.Station;
import subway.domain.station.StationDirection;
import subway.domain.station.Stations;
import subway.dto.*;
import subway.repository.GeneralSectionRepository;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class StationSaveService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final GeneralSectionRepository generalSectionRepository;

    public StationSaveService(final LineRepository lineRepository, final StationRepository stationRepository,
                              final GeneralSectionRepository generalSectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.generalSectionRepository = generalSectionRepository;
    }

    public StationSaveResponse saveStation(StationRequest stationRequest) {
        Optional<Line> findNullableLine = lineRepository.findLineByName(stationRequest.getLineName());
        if (findNullableLine.isEmpty()) {
            return saveInitialStations(stationRequest);
        }
        Line line = findNullableLine.get();
        List<Station> findStationsByLine = stationRepository.findAllStationByLineId(line.getId());
        Stations stations = new Stations(findStationsByLine);

        return saveStationAsStationCase(stationRequest, stations);
    }

    private StationSaveResponse saveInitialStations(StationRequest stationRequest) {
        Line savedLine = lineRepository.saveLine(new Line(null, stationRequest.getLineName()));

        Station savedUpStation = stationRepository.saveStation(new Station(null, stationRequest.getUpStationName(), savedLine));
        Station savedDownStation = stationRepository.saveStation(new Station(null, stationRequest.getDownStationName(), savedLine));

        GeneralSection sectionToSave = NewGeneralSectionMaker.makeSectionToSave(
                savedUpStation, savedDownStation, new Distance(stationRequest.getDistance())
        );
        GeneralSection savedSection = generalSectionRepository.saveSection(sectionToSave);

        return new StationSaveResponse(
                LineDto.from(savedLine),
                List.of(StationSaveDto.from(savedUpStation), StationSaveDto.from(savedDownStation)),
                List.of(GeneralSectionDto.from(savedSection))
        );
    }

    private StationSaveResponse saveStationAsStationCase(StationRequest stationRequest, Stations stations) {
        StationDirection stationDirection =
                stations.determineStationDirectionBy(stationRequest.getUpStationName(), stationRequest.getDownStationName());

        if (stationDirection == StationDirection.UP) {
            return saveStationAndSectionWhenUpStation(stationRequest, stations);
        }
        return saveStationAndSectionWhenDownStation(stationRequest, stations);
    }

    private StationSaveResponse saveStationAndSectionWhenUpStation(StationRequest stationRequest, Stations stations) {
        Line line = stations.getLineWhenAllSameLine();
        GeneralSections generalSections = new GeneralSections(generalSectionRepository.findAllSectionByLineId(line.getId()));

        Station currentDownStation = stations.findStationByStationName(stationRequest.getDownStationName()).get();
        Station savedUpStation = stationRepository.saveStation(new Station(null, stationRequest.getUpStationName(), line));
        Optional<GeneralSection> currentSectionHasRequestDownStationAsDownStation =
                generalSections.findSectionHasDownStationNameAsDownStationByLine(stationRequest.getDownStationName(), line);

        GeneralSection sectionToSave =
                NewGeneralSectionMaker.makeSectionToSave(savedUpStation, currentDownStation, new Distance(stationRequest.getDistance()));

        boolean isSavedStationMiddle = currentSectionHasRequestDownStationAsDownStation.isPresent();
        if (isSavedStationMiddle) {
            return saveSectionWhenUpStationMiddle(savedUpStation, currentSectionHasRequestDownStationAsDownStation, sectionToSave);
        }

        return saveSectionWhenEnd(savedUpStation, sectionToSave);
    }

    private StationSaveResponse saveSectionWhenUpStationMiddle(Station savedUpStation, Optional<GeneralSection> currentSectionHasRequestDownStationAsDownStation,
                                                               GeneralSection sectionToSave) {
        GeneralSection currentSection = currentSectionHasRequestDownStationAsDownStation.get();
        Line line = currentSection.getLine();
        generalSectionRepository.removeSectionById(currentSection.getId());
        GeneralSection newUpSection = NewGeneralSectionMaker.makeNewUpSection(sectionToSave, currentSection);
        GeneralSection savedNewUpSection = generalSectionRepository.saveSection(newUpSection);
        GeneralSection savedNewDownSection = generalSectionRepository.saveSection(sectionToSave);
        return new StationSaveResponse(LineDto.from(line),
                List.of(StationSaveDto.from(savedUpStation)),
                List.of(GeneralSectionDto.from(savedNewUpSection), GeneralSectionDto.from(savedNewDownSection)));
    }

    private StationSaveResponse saveSectionWhenEnd(Station savedDownStation, GeneralSection sectionToSave) {
        GeneralSection savedNewSection = generalSectionRepository.saveSection(sectionToSave);
        Line line = savedNewSection.getLine();

        return new StationSaveResponse(LineDto.from(line),
                List.of(StationSaveDto.from(savedDownStation)),
                List.of(GeneralSectionDto.from(savedNewSection)));
    }

    private StationSaveResponse saveStationAndSectionWhenDownStation(StationRequest stationRequest, Stations stations) {
        Line line = stations.getLineWhenAllSameLine();
        GeneralSections generalSections = new GeneralSections(generalSectionRepository.findAllSectionByLineId(line.getId()));

        Station currentUpStation = stations.findStationByStationName(stationRequest.getUpStationName()).get();
        Station savedDownStation = stationRepository.saveStation(new Station(null, stationRequest.getDownStationName(), line));
        Optional<GeneralSection> currentSectionHasRequestUpStationAsUpStation =
                generalSections.findSectionHasUpStationNameAsUpStationByLine(stationRequest.getUpStationName(), line);
        GeneralSection sectionToSave =
                NewGeneralSectionMaker.makeSectionToSave(currentUpStation, savedDownStation, new Distance(stationRequest.getDistance()));

        if (currentSectionHasRequestUpStationAsUpStation.isPresent()) {
            return saveSectionWhenDownStationMiddle(savedDownStation, currentSectionHasRequestUpStationAsUpStation, sectionToSave);
        }
        return saveSectionWhenEnd(savedDownStation, sectionToSave);
    }

    private StationSaveResponse saveSectionWhenDownStationMiddle(Station savedDownStation,
                                                                 Optional<GeneralSection> currentSectionHasRequestUpStationAsUpStation, GeneralSection sectionToSave) {
        GeneralSection currentSection = currentSectionHasRequestUpStationAsUpStation.get();
        Line line = currentSection.getLine();

        generalSectionRepository.removeSectionById(currentSection.getId());
        GeneralSection savedNewUpSection = generalSectionRepository.saveSection(sectionToSave);
        GeneralSection newDownSection = NewGeneralSectionMaker.makeNewDownSection(sectionToSave, currentSection);
        GeneralSection savedNewDownSection = generalSectionRepository.saveSection(newDownSection);
        return new StationSaveResponse(LineDto.from(line),
                List.of(StationSaveDto.from(savedDownStation)),
                List.of(GeneralSectionDto.from(savedNewUpSection), GeneralSectionDto.from(savedNewDownSection)));
    }
}

