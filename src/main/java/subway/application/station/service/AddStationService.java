package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.usecase.AddStationUseCase;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;
import subway.domain.station.StationRepository;
import subway.exception.NoDataFoundException;
import subway.ui.dto.request.AddStationRequest;

@Transactional
@Service
public class AddStationService implements AddStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public AddStationService(final LineRepository lineRepository, final SectionRepository sectionRepository,
                             final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Long addStation(final Long lineId, final AddStationRequest request) {
        final Line line = lineRepository.findById(lineId);

        if (request.getFirstStationId() == null) {
            return addStationAsFront(line, request);
        }

        if (request.getSecondStationId() == null) {
            return addStationAsEnd(line, request);
        }

        return insertStation(line, request);
    }

    private Long addStationAsFront(final Line line, final AddStationRequest request) {
        final Station newStation = new Station(request.getNewStation());
        final Station standartStation = stationRepository.findById(request.getSecondStationId())
                .orElseThrow(NoDataFoundException::new);
        final StationDistance distance = new StationDistance(request.getDistance());
        validateIsFrontStation(line.getFrontStation(), standartStation);

        final Station savedStation = stationRepository.saveIfNotExist(newStation);
        final Section section = new Section(savedStation, standartStation, distance);
        line.addSection(section);

        sectionRepository.save(section, line.getId());
        return savedStation.getStationId();
    }

    private void validateIsFrontStation(final Station frontStation, final Station standartStation) {
        if (!standartStation.matchStationName(frontStation)) {
            throw new IllegalStateException("기준역이 상행 종점이 아닙니다: " + standartStation.getStationName());
        }
    }

    private Long addStationAsEnd(final Line line, final AddStationRequest request) {
        final Station standartStation = stationRepository.findById(request.getFirstStationId())
                .orElseThrow(NoDataFoundException::new);
        final Station newStation = new Station(request.getNewStation());
        final StationDistance distance = new StationDistance(request.getDistance());
        validateIsEndStation(line.getEndStation(), standartStation);

        final Station savedStation = stationRepository.saveIfNotExist(newStation);

        final Section section = new Section(standartStation, savedStation, distance);
        line.addSection(section);
        sectionRepository.save(section, line.getId());
        return savedStation.getStationId();
    }

    private void validateIsEndStation(final Station endStation, final Station standartStation) {
        if (!standartStation.matchStationName(endStation)) {
            throw new IllegalStateException("기준역이 하행 종점이 아닙니다: " + standartStation.getStationName());
        }
    }

    private Long insertStation(final Line line, final AddStationRequest request) {
        final Station standardStation = stationRepository.findById(request.getFirstStationId())
                .orElseThrow(NoDataFoundException::new);
        final Station secondStation = stationRepository.findById(request.getSecondStationId())
                .orElseThrow(NoDataFoundException::new);
        final Station newStation = new Station(request.getNewStation());
        validateIsConnectedSection(line, standardStation, secondStation);

        final Sections sections = line.getSections();
        final Section removedSection = sections.peekByFirstStationUnique(standardStation);
        final Station savedStation = stationRepository.saveIfNotExist(newStation);
        sections.insertBehindStation(standardStation, savedStation, new StationDistance(request.getDistance()));

        sectionRepository.delete(removedSection);

        saveSeparatedSections(line.getId(), savedStation, sections);
        return savedStation.getStationId();
    }

    private void saveSeparatedSections(final Long lineId, final Station newStation, final Sections sections) {
        final Section leftSection = sections.peekBySecondStationUnique(newStation);
        final Section rightSection = sections.peekByFirstStationUnique(newStation);
        sectionRepository.save(leftSection, lineId);
        sectionRepository.save(rightSection, lineId);
    }

    private void validateIsConnectedSection(final Line line, final Station firstStation, final Station secondStation) {
        final Sections sections = line.getSections();
        final Section section = sections.peekByFirstStationUnique(firstStation);

        if (!section.getSecondStation().matchStationName(secondStation)) {
            throw new IllegalStateException("두 역은 구간으로 연결되어 있지 않습니다: "
                    + firstStation.getStationName()
                    + ", "
                    + secondStation.getStationName()
            );
        }
    }
}
