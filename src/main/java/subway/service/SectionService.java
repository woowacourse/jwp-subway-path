package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.request.SectionCreationRequest;
import subway.repository.LineRepository;

@Service
@Transactional
public class SectionService {

    private final LineRepository lineRepository;
    private final StationDao stationDao;

    public SectionService(final LineRepository lineRepository, final StationDao stationDao) {
        this.lineRepository = lineRepository;
        this.stationDao = stationDao;
    }

    public void saveSection(final SectionCreationRequest request) {
        Line line = findLineByLineId(request.getLineId());
        Station upwardStation = findStationByStationId(request.getUpwardStationId());
        Station downwardStation = findStationByStationId(request.getDownwardStationId());

        line.addSection(upwardStation, downwardStation, request.getDistance());
        lineRepository.update(line);
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

        line.removeStation(station);
        lineRepository.update(line);
    }
}
