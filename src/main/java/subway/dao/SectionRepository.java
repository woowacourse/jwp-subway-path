package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.custom.LineNotExistException;
import subway.exception.custom.StationNotExistException;

@Repository
public class SectionRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionRepository(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Section create(final Long lineId, final Section section) {
        return sectionDao.insert(lineId, section);
    }

    public List<Section> findAllByLineId(final Long lineId) {
        if (lineDao.findById(lineId).isEmpty()) {
            throw new LineNotExistException("역을 등록하려는 노선이 존재하지 않습니다.");
        }
        return sectionDao.findAllByLineId(lineId);
    }

    public Long findId(final Long lineId, final Section section) {
        final String upStationName = section.getUpStation().getName();
        final String downStationName = section.getDownStation().getName();

        final Station upStation = stationDao.findByName(upStationName)
            .orElseThrow(() -> new StationNotExistException("해당 이름의 역이 존재하지 않습니다. ( " + upStationName + " )"));
        final Station downStation = stationDao.findByName(downStationName)
            .orElseThrow(() -> new StationNotExistException("해당 이름의 역이 존재하지 않습니다. ( " + downStationName + " )"));

        return sectionDao.findId(lineId, Section.of(upStation, downStation, section.getDistance()));
    }

    public void deleteAllByLineId(final Long lineId) {
        sectionDao.deleteAllByLineId(lineId);
    }

    public Station registerStation(final String stationName) {
        return stationDao.insert(Station.from(stationName));
    }

    public Station findStationByName(final String stationName) {
        return stationDao.findByName(stationName)
            .orElseThrow(() -> new StationNotExistException("해당 이름의 역이 존재하지 않습니다. ( " + stationName + " )"));
    }

    public Station registerStationIfNotExist(final String stationName) {
        final Optional<Station> savedStation = stationDao.findByName(stationName);
        return savedStation.orElseGet(() -> stationDao.insert(Station.from(stationName)));
    }
}
