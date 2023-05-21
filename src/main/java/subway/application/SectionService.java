package subway.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class SectionService {

    private static final String NO_SUCH_STATION_MESSAGE = "해당하는 역이 존재하지 않습니다.";
    private static final String NO_SUCH_LINE_MESSAGE = "해당하는 호선이 존재하지 않습니다.";

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(final SectionRepository sectionRepository, final StationRepository stationRepository,
                          final LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public void save(final SectionRequest sectionRequest) {
        validateExists(sectionRequest);
        final Sections sections = findSections(sectionRequest.getLineId());

        final Station upStation = stationRepository.findById(sectionRequest.getUpStationId());
        final Station downStation = stationRepository.findById(sectionRequest.getDownStationId());
        final Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        sections.addSection(section);

        sectionRepository.deleteAllByLineId(sectionRequest.getLineId());
        sectionRepository.insertAll(sectionRequest.getLineId(), sections);
    }

    private void validateExists(final SectionRequest sectionRequest) {
        if (lineRepository.notExistsById(sectionRequest.getLineId())) {
            throw new NoSuchElementException(NO_SUCH_LINE_MESSAGE);
        }
        if (stationRepository.notExistsById(sectionRequest.getUpStationId())
                || stationRepository.notExistsById(sectionRequest.getDownStationId())) {
            throw new NoSuchElementException(NO_SUCH_STATION_MESSAGE);
        }
    }

    public void delete(final Long lineId, final Long stationId) {
        final Sections sections = findSections(lineId);
        final Station station = stationRepository.findById(stationId);
        sections.removeSectionByStation(station);

        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.insertAll(lineId, sections);
    }

    private Sections findSections(final Long lineId) {
        return sectionRepository.findByLineId(lineId);
    }

    public List<SectionResponse> findByLineId(final Long lineId) {
        return sectionRepository.findByLineId(lineId).getSections().stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
    }
}
