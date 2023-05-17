package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.AddOneSectionRequest;
import subway.dto.AddTwoSectionRequest;
import subway.repository.SectionRepository;

@Service
@Transactional
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionRepository sectionRepository;

    public SectionService(final LineDao lineDao, final StationDao stationDao, final SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionRepository = sectionRepository;
    }

    public void addOneSection(final Long lineId, final AddOneSectionRequest addOneSectionRequest) {
        Line line = lineDao.findById(lineId);
        Station upStation = stationDao.findById(addOneSectionRequest.getUpStationId());
        Station downStation = stationDao.findById(addOneSectionRequest.getDownStationId());
        Distance distance = new Distance(addOneSectionRequest.getDistance());

        Section section = new Section(line, upStation, downStation, distance);

        Sections sections = sectionRepository.findByLine(line);
        sections.add(section);

        sectionRepository.save(section);
    }

    public void addTwoSections(final Long lineId, final AddTwoSectionRequest addTwoSectionRequest) {
        Line line = lineDao.findById(lineId);
        Station newStation = stationDao.findById(addTwoSectionRequest.getNewStationId());
        Station upStation = stationDao.findById(addTwoSectionRequest.getUpStationId());
        Station downStation = stationDao.findById(addTwoSectionRequest.getDownStationId());
        Distance upDistance = new Distance(addTwoSectionRequest.getUpStationDistance());
        Distance downDistance = new Distance(addTwoSectionRequest.getDownStationDistance());

        Section upSection = new Section(line, upStation, newStation, upDistance);
        Section downSection = new Section(line, newStation, downStation, downDistance);

        Sections sections = sectionRepository.findAll();
        sections.addTwoSections(upSection, downSection);

        sectionRepository.update(lineId, sections);
    }

    public void removeStation(final Long lineId, final Long stationId) {
        Line line = lineDao.findById(lineId);
        Station station = stationDao.findById(stationId);

        Sections sections = sectionRepository.findByLine(line);

        sections.removeStation(line, station);

        sectionRepository.update(lineId, sections);
    }
}
