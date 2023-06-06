package subway.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.SectionDirection;
import subway.domain.subwaymap.Sections;
import subway.domain.subwaymap.Station;
import subway.dto.request.SectionCreateRequest;
import subway.exception.custom.LineNotExistException;
import subway.exception.custom.StationNotExistException;

@Service
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(final LineDao lineDao, final StationDao stationDao,
        final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public Long create(final Long lineId, final SectionCreateRequest request) {
        if (lineDao.findById(lineId).isEmpty()) {
            throw new LineNotExistException("역을 등록하려는 노선이 존재하지 않습니다.");
        }

        final Sections sections = Sections.of(sectionDao.findAllByLineId(lineId));
        final Station baseStation = insertStationIfNotExist(request.getBaseStation());
        final Station newStation = insertStationIfNotExist(request.getNewStation());
        final Section newSection = Section.of(baseStation, newStation,
            SectionDirection.get(request.getDirection()), request.getDistance());

        sections.addSection(baseStation, newSection);
        updateSectionsInLine(lineId, sections.getSections());

        return findSectionId(lineId, newSection);
    }

    private Station insertStationIfNotExist(final String stationName) {
        final Optional<Station> savedStation = stationDao.findByName(stationName);
        return savedStation.orElseGet(() -> stationDao.insert(Station.from(stationName)));
    }

    private Long findSectionId(final Long lineId, final Section section) {
        final String upStationName = section.getUpStation().getName();
        final String downStationName = section.getDownStation().getName();

        final Station upStation = findExistStation(upStationName);
        final Station downStation = findExistStation(downStationName);

        return sectionDao.findIdBy(lineId, Section.withNullId(upStation, downStation, section.getDistance()));
    }

    private Station findExistStation(final String name) {
        return stationDao.findByName(name)
            .orElseThrow(() -> new StationNotExistException("해당 이름의 역이 존재하지 않습니다. ( " + name + " )"));
    }

    @Transactional
    public void delete(final Long lineId, final String stationName) {
        if (lineDao.findById(lineId).isEmpty()) {
            throw new LineNotExistException("역을 등록하려는 노선이 존재하지 않습니다.");
        }
        final Sections sections = Sections.of(sectionDao.findAllByLineId(lineId));
        final Station findStation = sections.getStationByName(stationName);
        sections.removeStation(findStation);
        updateSectionsInLine(lineId, sections.getSections());
    }

    private void updateSectionsInLine(final Long lineId, final List<Section> sections) {
        sectionDao.deleteAllByLineId(lineId);
        sections.forEach(section -> sectionDao.insert(lineId, section));
    }
}
