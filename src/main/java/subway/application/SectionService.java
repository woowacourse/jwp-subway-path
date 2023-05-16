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
import subway.repository.SectionRepository;
import java.util.List;

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

    public void addTwoStations(final Long lineId, final AddOneSectionRequest addOneSectionRequest) {
        Line line = lineDao.findById(lineId);
        Station upStation = stationDao.findById(addOneSectionRequest.getUpStationId());
        Station downStation = stationDao.findById(addOneSectionRequest.getDownStationId());
        Distance distance = new Distance(addOneSectionRequest.getDistance());

        Section section = new Section(line, upStation, downStation, distance);

        List<Section> savedSections = sectionRepository.findAll();
        Sections sections = new Sections(savedSections);
        sections.add(section);

        sectionRepository.save(section);
    }
}
