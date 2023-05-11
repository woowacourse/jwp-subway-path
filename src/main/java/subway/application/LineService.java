package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.SectionDto;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public List<LineResponse> findAllLines() {
        final List<Line> lines = lineDao.findAll();

        return lines.stream()
                .map(Line::getName)
                .map(this::findStationsByLineName)
                .collect(Collectors.toList());
    }

    public LineResponse findStationsByLineName(final String lineName) {
        final Line line = lineDao.findByName(lineName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선명입니다."));

        final List<SectionDto> sectionDtos = sectionDao.findByLineId(line.getId());

        final Map<Long, Long> upToDown = sectionDtos.stream()
                .collect(Collectors.toMap(SectionDto::getUpStationId, SectionDto::getDownStationId));

        final Map<Long, Station> stationsById = new HashMap<>();

        final List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            stationsById.put(station.getId(), station);
        }

        final Long firstStationIdByLineId = sectionDao.findFirstStationIdByLineId(line.getId());
        final List<Station> sortedStation = new ArrayList<>();

        Long currId = firstStationIdByLineId;
        while(upToDown.containsKey(currId)) {
            Long nextId = upToDown.get(currId);
            sortedStation.add(stationsById.get(currId));
            currId = nextId;
        }
        sortedStation.add(stationsById.get(currId));

        final List<StationResponse> stationResponses = sortedStation.stream()
                .map(station -> new StationResponse(
                        station.getId(),
                        station.getName()
                ))
                .collect(Collectors.toList());

        return LineResponse.of(line, stationResponses);
    }
}
