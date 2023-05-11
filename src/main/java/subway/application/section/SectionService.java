package subway.application.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.section.dto.SectionCreateRequest;
import subway.application.section.dto.SectionCreateResponse;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public SectionCreateResponse insert(SectionCreateRequest sectionCreateRequest, long lineId) {
        Station upStation = new Station(sectionCreateRequest.getUpStationName());
        Station downStation = new Station(sectionCreateRequest.getDownStationName());
        Distance distance = new Distance(sectionCreateRequest.getDistance());
        Line line = lineDao.findById(lineId);

        Section section = new Section(upStation, downStation, distance);
        Section savedSection = sectionDao.insert(section, line);
        return SectionCreateResponse.of(savedSection);
    }
}
