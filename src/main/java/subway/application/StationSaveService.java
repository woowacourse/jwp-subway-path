package subway.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.section.Distance;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.general.GeneralSections;
import subway.domain.section.general.NewGeneralSectionMaker;
import subway.domain.station.Station;
import subway.domain.station.StationDirection;
import subway.domain.station.StationsByLine;
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
        List<Station> findStations = stationRepository.findAllStation();
        StationsByLine stationsByLine = StationsByLine.of(findLines, findStations);
        Line line = findNullableLine.get();

        return saveStationAsStationCase(stationRequest, stationsByLine, line);
    }

    private StationSaveResponse saveInitialStations(StationRequest stationRequest) {
        Line savedLine = lineRepository.saveLine(new Line(null, stationRequest.getLineName()));

        Station savedUpStation = stationRepository.saveStation(new Station(null, stationRequest.getUpStationName(), savedLine));
        Station SavedDownStation = stationRepository.saveStation(new Station(null, stationRequest.getDownStationName(), savedLine));

        GeneralSection sectionToSave = NewGeneralSectionMaker.makeSectionToSave(
                savedLine, savedUpStation,
                SavedDownStation, new Distance(stationRequest.getDistance())
        );
        GeneralSection savedSection = generalSectionRepository.saveSection(sectionToSave);

        return new StationSaveResponse(
                LineDto.from(savedLine),
                List.of(StationSaveDto.from(savedUpStation), StationSaveDto.from(SavedDownStation)),
                List.of(GeneralSectionDto.from(savedSection))
        );
    }

    private StationSaveResponse saveStationAsStationCase(StationRequest stationRequest, StationsByLine stationsByLine, Line line) {
        StationDirection stationDirection =
                stationsByLine.determineStationDirectionBy(stationRequest.getUpStationName(), stationRequest.getDownStationName(), line);

        if (stationDirection == StationDirection.UP) {
            return saveStationAndSectionWhenUpStation(stationRequest, stationsByLine, line);
        }
        return saveStationAndSectionWhenDownStation(stationRequest, stationsByLine, line);
    }

    private StationSaveResponse saveStationAndSectionWhenUpStation(StationRequest stationRequest, StationsByLine stationsByLine, Line line) {
        GeneralSections generalSections = new GeneralSections(generalSectionRepository.findAllSectionByLineId(line.getId()));

        Station currentDownStation = stationsByLine.findStationByStationNameAndLine(stationRequest.getDownStationName(), line);
        Station savedUpStation = stationRepository.saveStation(new Station(null, stationRequest.getUpStationName(), line));
        Optional<GeneralSection> currentSectionHasRequestDownStationAsDownStation =
                generalSections.findSectionHasDownStationNameAsDownStationByLine(stationRequest.getDownStationName(), line);

        GeneralSection sectionToSave =
                NewGeneralSectionMaker.makeSectionToSave(line, savedUpStation, currentDownStation, new Distance(stationRequest.getDistance()));

        boolean isSavedStationMiddle = currentSectionHasRequestDownStationAsDownStation.isPresent();
        if (isSavedStationMiddle) {
            return saveSectionWhenUpStationMiddle(line, savedUpStation, currentSectionHasRequestDownStationAsDownStation, sectionToSave);
        }

        return saveSectionWhenEnd(line, savedUpStation, sectionToSave);
    }

    private StationSaveResponse saveSectionWhenUpStationMiddle(Line line, Station savedUpStation, Optional<GeneralSection> currentSectionHasRequestDownStationAsDownStation, GeneralSection sectionToSave) {
        GeneralSection currentSection = currentSectionHasRequestDownStationAsDownStation.get();

        generalSectionRepository.removeSectionById(currentSection.getId());
        GeneralSection newUpSection = NewGeneralSectionMaker.makeNewUpSection(sectionToSave, currentSection);
        GeneralSection savedNewUpSection = generalSectionRepository.saveSection(newUpSection);
        GeneralSection savedNewDownSection = generalSectionRepository.saveSection(sectionToSave);
        return new StationSaveResponse(LineDto.from(line),
                List.of(StationSaveDto.from(savedUpStation)),
                List.of(GeneralSectionDto.from(savedNewUpSection), GeneralSectionDto.from(savedNewDownSection)));
    }

    private StationSaveResponse saveSectionWhenEnd(Line line, Station savedDownStation, GeneralSection sectionToSave) {
        GeneralSection savedNewSection = generalSectionRepository.saveSection(sectionToSave);

        return new StationSaveResponse(LineDto.from(line),
                List.of(StationSaveDto.from(savedDownStation)),
                List.of(GeneralSectionDto.from(savedNewSection)));
    }

    private StationSaveResponse saveStationAndSectionWhenDownStation(StationRequest stationRequest, StationsByLine stationsByLine, Line line) {
        GeneralSections generalSections = new GeneralSections(generalSectionRepository.findAllSectionByLineId(line.getId()));

        Station currentUpStation = stationsByLine.findStationByStationNameAndLine(stationRequest.getUpStationName(), line);
        Station savedDownStation = stationRepository.saveStation(new Station(null, stationRequest.getDownStationName(), line));
        Optional<GeneralSection> currentSectionHasRequestUpStationAsUpStation =
                generalSections.findSectionHasUpStationNameAsUpStationByLine(stationRequest.getUpStationName(), line);
        GeneralSection sectionToSave =
                NewGeneralSectionMaker.makeSectionToSave(line, currentUpStation, savedDownStation, new Distance(stationRequest.getDistance()));

        if (currentSectionHasRequestUpStationAsUpStation.isPresent()) {
            return saveSectionWhenDownStationMiddle(line, savedDownStation, currentSectionHasRequestUpStationAsUpStation, sectionToSave);
        }
        return saveSectionWhenEnd(line, savedDownStation, sectionToSave);
    }

    private StationSaveResponse saveSectionWhenDownStationMiddle(Line line, Station savedDownStation,
                                                                 Optional<GeneralSection> currentSectionHasRequestUpStationAsUpStation, GeneralSection sectionToSave) {
        GeneralSection currentSection = currentSectionHasRequestUpStationAsUpStation.get();

        generalSectionRepository.removeSectionById(currentSection.getId());
        GeneralSection savedNewUpSection = generalSectionRepository.saveSection(sectionToSave);
        GeneralSection newDownSection = NewGeneralSectionMaker.makeNewDownSection(sectionToSave, currentSection);
        GeneralSection savedNewDownSection = generalSectionRepository.saveSection(newDownSection);
        return new StationSaveResponse(LineDto.from(line),
                List.of(StationSaveDto.from(savedDownStation)),
                List.of(GeneralSectionDto.from(savedNewUpSection), GeneralSectionDto.from(savedNewDownSection)));
    }
}

