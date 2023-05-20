package subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.*;
import subway.domain.*;
import subway.dto.SectionRequest;
import subway.exception.InvalidStationException;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest request) {
        List<Section> sections = sectionDao.findByLineId(lineId);
        Line line = new Line(lineDao.findById(lineId).get().getName(),new Sections(sections));

        addStationOfSection(new Station(request.getStartStation()), new Station(request.getEndStation()));

        Section requestSection = new Section(
                stationDao.findByName(request.getStartStation()),
                stationDao.findByName(request.getEndStation()),
                new Distance(request.getDistance()));

        line.addSection(requestSection);
        sectionDao.deleteAllById(lineId);
        sectionDao.insertAll(lineId, line.getSections().getSections());
    }

    private void addStationOfSection(Station requestStartStation, Station requestEndStation) {
        Map<String ,Long> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(Station::getName,Station::getId));

        if (!stations.containsKey(requestStartStation.getName())) {
            stationDao.insert(new Station(requestStartStation.getName()));
        }

        if (!stations.containsKey(requestEndStation.getName())) {
            stationDao.insert(new Station(requestEndStation.getName()));
        }
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        List<Section> sections = sectionDao.findByLineId(lineId);
        Line line = new Line(lineDao.findById(lineId).get().getName(),new Sections(sections));

        line.getSections().remove(stationDao.findById(stationId).orElseThrow(InvalidStationException::new));

        sectionDao.deleteAllById(lineId);
        sectionDao.insertAll(lineId, line.getSections().getSections());

        deleteStation(stationId);
    }

    private void deleteStation(Long stationId) {
        if(!sectionDao.findExistStationById(stationId)){
            stationDao.deleteById(stationId);
        }
    }
}
