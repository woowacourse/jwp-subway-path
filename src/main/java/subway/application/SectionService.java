package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.SectionCreateDto;
import subway.application.dto.SectionDeleteDto;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;
import subway.dto.section.SectionResponse;
import subway.exception.IllegalDistanceException;
import subway.exception.IllegalSectionException;
import subway.exception.StationNotFoundException;

@Service
@Transactional
public class SectionService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository,
                          SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public void saveSection(SectionCreateDto requestedSection) {
        Station startStation = stationRepository.findByName(requestedSection.getStartStationName());
        Station endStation = stationRepository.findByName(requestedSection.getEndStationName());
        Line line = lineRepository.findById(requestedSection.getLineId());
        addSection(line, new Section(startStation, endStation, requestedSection.getDistance()));
    }

    private void addSection(Line line, Section sectionToAdd) {
        if (line.isEmpty()) {
            sectionRepository.save(line.getId(), sectionToAdd);
            return;
        }
        addSectionByExistence(line, sectionToAdd);
    }

    private void addSectionByExistence(Line line, Section sectionToAdd) {
        boolean isStartStationAddRequest = line.hasStationInLine(sectionToAdd.getEndStation());
        boolean isEndStationAddRequest = line.hasStationInLine(sectionToAdd.getStartStation());
        validateAddableSection(isStartStationAddRequest, isEndStationAddRequest);
        if (isStartStationAddRequest) {
            addStartStation(line, sectionToAdd);
        }
        if (isEndStationAddRequest) {
            addEndStation(line, sectionToAdd);
        }
    }

    private void validateAddableSection(boolean hasStartStation, boolean hasEndStation) {
        if (hasStartStation && hasEndStation) {
            throw new IllegalSectionException("해당 노선에 이미 존재하는 구간입니다.");
        }
        if (!hasStartStation && !hasEndStation) {
            throw new IllegalSectionException("해당 노선에 추가할 수 없는 역입니다.");
        }
    }

    private void addStartStation(Line line, Section sectionToAdd) {
        Optional<Section> prevExistingSection = line.findSectionWithEndStation(sectionToAdd.getEndStation());
        prevExistingSection.ifPresent(prevSection -> {
            changePrevSectionToNewSection(prevSection, sectionToAdd);
            saveStation(line.getId(), prevSection.getStartStation(), sectionToAdd.getStartStation(),
                    prevSection.getDistance() - sectionToAdd.getDistance());
        });
        sectionRepository.save(line.getId(), sectionToAdd);
    }

    private void changePrevSectionToNewSection(Section prevSection, Section newSection) {
        validateDistance(prevSection.getDistance(), newSection.getDistance());
        sectionRepository.delete(prevSection);
    }

    private void saveStation(Long lineId, Station startStation, Station endStation, int distance) {
        sectionRepository.save(lineId, new Section(startStation, endStation, distance));
    }

    private void addEndStation(Line line, Section sectionToAdd) {
        Optional<Section> prevExistingSection = line.findSectionWithStartStation(
                sectionToAdd.getStartStation()); // 로직 다름
        prevExistingSection.ifPresent(prevSection -> {
            changePrevSectionToNewSection(prevSection, sectionToAdd);
            saveStation(line.getId(), sectionToAdd.getEndStation(), prevSection.getEndStation(),
                    prevSection.getDistance() - sectionToAdd.getDistance());
        });
        sectionRepository.save(line.getId(), sectionToAdd);
    }

    private void validateDistance(int distance, int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalDistanceException("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다.");
        }
    }

    public void deleteSection(SectionDeleteDto requestedSection) {
        Line line = lineRepository.findById(requestedSection.getId());
        Station station = stationRepository.findByName(requestedSection.getStationName());
        validateStationInLine(line, station);
        Optional<Section> backSection = line.findSectionWithStartStation(station);
        Optional<Section> frontSection = line.findSectionWithEndStation(station);
        if (backSection.isPresent() && frontSection.isPresent()) {
            mergeSection(requestedSection.getId(), frontSection.get(), backSection.get());
        }
        frontSection.ifPresent(sectionRepository::delete);
        backSection.ifPresent(sectionRepository::delete);
    }

    private void validateStationInLine(Line line, Station station) {
        if (line.hasStationInLine(station)) {
            return;
        }
        throw new StationNotFoundException();
    }

    private void mergeSection(Long lineId, Section frontSection, Section backSection) {
        Section mergedSection = new Section(frontSection.getStartStation(), backSection.getEndStation(),
                frontSection.getDistance() + backSection.getDistance());
        sectionRepository.save(lineId, mergedSection);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return line.getSections().stream()
                .map(SectionResponse::from)
                .collect(toList());
    }
}
