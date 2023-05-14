package subway.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.section.Distance;
import subway.domain.section.NewSectionMaker;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDirection;
import subway.domain.station.StationsByLine;
import subway.dto.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class StationSaveService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public StationSaveService(final LineRepository lineRepository, final StationRepository stationRepository,
                              final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    // TODO : 중간역 저장 시 상행역, 하행역 등록 분기에서 구간 거리만 다른데 분기를 나누지 않고 하나로 합칠 방법은 없을까?
    // TODO : Optional.get() -> Optional.orElse(), Optional.orElseGet() 으로 리팩토링
    // TODO : 이미 있는 노선, 역, 구간 저장 시 예외
    public StationSaveResponse saveStation(StationRequest stationRequest) {
        List<Line> findLines = lineRepository.findAllLine();
        Lines lines = new Lines(findLines);
        Optional<Line> findNullableLine = lines.findByLineName(stationRequest.getLineName());
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

        Section sectionToSave = NewSectionMaker.makeSectionToSave(
                savedLine, savedUpStation,
                SavedDownStation, new Distance(stationRequest.getDistance())
        );
        Section savedSection = sectionRepository.saveSection(sectionToSave);

        return new StationSaveResponse(
                LineDto.from(savedLine),
                List.of(StationDto.from(savedUpStation), StationDto.from(SavedDownStation)),
                List.of(SectionDto.from(savedSection))
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
        Sections sections = new Sections(sectionRepository.findAllSectionByLineId(line.getId()));

        Station currentDownStation = stationsByLine.findStationByStationNameAndLine(stationRequest.getDownStationName(), line);
        Station savedUpStation = stationRepository.saveStation(new Station(null, stationRequest.getUpStationName(), line));
        Optional<Section> currentSectionHasRequestDownStationAsDownStation =
                sections.findCurrentSectionHasRequestDownStationNameAsDownStationByLine(stationRequest.getDownStationName(), line);

        Section sectionToSave =
                NewSectionMaker.makeSectionToSave(line, savedUpStation, currentDownStation, new Distance(stationRequest.getDistance()));

        boolean isSavedStationMiddle = currentSectionHasRequestDownStationAsDownStation.isPresent();
        if (isSavedStationMiddle) {
            return saveSectionWhenUpStationMiddle(line, savedUpStation, currentSectionHasRequestDownStationAsDownStation, sectionToSave);
        }

        return saveSectionWhenEnd(line, savedUpStation, sectionToSave);
    }

    private StationSaveResponse saveSectionWhenUpStationMiddle(Line line, Station savedUpStation, Optional<Section> currentSectionHasRequestDownStationAsDownStation, Section sectionToSave) {
        Section currentSection = currentSectionHasRequestDownStationAsDownStation.get();

        sectionRepository.removeSectionById(currentSection.getId());
        Section newUpSection = NewSectionMaker.makeNewUpSection(sectionToSave, currentSection);
        Section savedNewUpSection = sectionRepository.saveSection(newUpSection);
        Section savedNewDownSection = sectionRepository.saveSection(sectionToSave);
        return new StationSaveResponse(LineDto.from(line),
                List.of(StationDto.from(savedUpStation)),
                List.of(SectionDto.from(savedNewUpSection), SectionDto.from(savedNewDownSection)));
    }

    private StationSaveResponse saveSectionWhenEnd(Line line, Station savedDownStation, Section sectionToSave) {
        Section savedNewSection = sectionRepository.saveSection(sectionToSave);

        return new StationSaveResponse(LineDto.from(line),
                List.of(StationDto.from(savedDownStation)),
                List.of(SectionDto.from(savedNewSection)));
    }

    private StationSaveResponse saveStationAndSectionWhenDownStation(StationRequest stationRequest, StationsByLine stationsByLine, Line line) {
        Sections sections = new Sections(sectionRepository.findAllSectionByLineId(line.getId()));

        Station currentUpStation = stationsByLine.findStationByStationNameAndLine(stationRequest.getUpStationName(), line);
        Station savedDownStation = stationRepository.saveStation(new Station(null, stationRequest.getDownStationName(), line));
        Optional<Section> currentSectionHasRequestUpStationAsUpStation =
                sections.findCurrentSectionHasRequestUpStationNameAsUpStationByLine(stationRequest.getUpStationName(), line);
        Section sectionToSave =
                NewSectionMaker.makeSectionToSave(line, currentUpStation, savedDownStation, new Distance(stationRequest.getDistance()));

        if (currentSectionHasRequestUpStationAsUpStation.isPresent()) {
            return saveSectionWhenDownStationMiddle(line, savedDownStation, currentSectionHasRequestUpStationAsUpStation, sectionToSave);
        }
        return saveSectionWhenEnd(line, savedDownStation, sectionToSave);
    }

    private StationSaveResponse saveSectionWhenDownStationMiddle(Line line, Station savedDownStation,
                                                                 Optional<Section> currentSectionHasRequestUpStationAsUpStation, Section sectionToSave) {
        Section currentSection = currentSectionHasRequestUpStationAsUpStation.get();

        sectionRepository.removeSectionById(currentSection.getId());
        Section savedNewUpSection = sectionRepository.saveSection(sectionToSave);
        Section newDownSection = NewSectionMaker.makeNewDownSection(sectionToSave, currentSection);
        Section savedNewDownSection = sectionRepository.saveSection(newDownSection);
        return new StationSaveResponse(LineDto.from(line),
                List.of(StationDto.from(savedDownStation)),
                List.of(SectionDto.from(savedNewUpSection), SectionDto.from(savedNewDownSection)));
    }
}

