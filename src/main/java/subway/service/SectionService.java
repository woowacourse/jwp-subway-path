package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.SectionCreationRequest;
import subway.repository.LineRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final LineRepository lineRepository, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineRepository = lineRepository;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void saveSection(final SectionCreationRequest request) {
        Line line = findLineByLineId(request.getLineId());
        Station upwardStation = findStationByStationId(request.getUpwardStationId());
        Station downwardStation = findStationByStationId(request.getDownwardStationId());

        //todo : 일부만 지우고 삽입하기
        line.addSection(upwardStation, downwardStation, request.getDistance());
        sectionDao.deleteByLineId(line.getId());
        for(Section section : line.getSections()){
            sectionDao.insert(section, line.getId());
        }
    }

    private Line findLineByLineId(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }

    private Station findStationByStationId(final long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    public void deleteSections(final long lineId, final long stationId) {
        Line line = findLineByLineId(lineId);
        Station station = findStationByStationId(stationId);

        //todo : 일부만 지우고 삽입하기
        line.removeStation(station);
        sectionDao.deleteByLineId(line.getId());
        for(Section section : line.getSections()){
            sectionDao.insert(section, line.getId());
        }
    }
}
