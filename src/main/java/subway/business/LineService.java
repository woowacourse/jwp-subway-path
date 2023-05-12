package subway.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.converter.SectionConverter;
import subway.business.domain.Section;
import subway.business.domain.Station;
import subway.business.domain.Stations;
import subway.persistence.LineDao;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.rowmapper.SectionDetail;
import subway.persistence.entity.SectionEntity;
import subway.business.converter.LineConverter;
import subway.business.dto.LineDto;
import subway.business.dto.SectionCreateDto;
import subway.persistence.entity.StationEntity;
import subway.presentation.dto.request.LineRequest;
import subway.presentation.dto.response.LineResponse;
import subway.persistence.entity.LineEntity;
import subway.presentation.dto.response.SingleLineDetailResponse;
import subway.presentation.query_option.SubwayDirection;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public long save(final LineDto lineDto, final SectionCreateDto sectionCreateDto) {
        final Long lineId = lineDao.insert(LineConverter.toEntity(lineDto));
        final StationEntity previousStation = stationDao.findByName(sectionCreateDto.getPreviousStation());
        final StationEntity nextStation = stationDao.findByName(sectionCreateDto.getNextStation());
        sectionDao.insert(new SectionEntity.Builder()
                .lineId(lineId)
                .distance(sectionCreateDto.getDistance())
                .previousStationId(previousStation.getId())
                .nextStationId(nextStation.getId())
                .build());
        return lineId;
    }

    public List<SingleLineDetailResponse> getAllLine() {
        return sectionDao.findSectionDetail().stream()
                .collect(Collectors.groupingBy(SectionDetail::getLineId))
                .values().stream()
                .map(this::convert)
                .collect(Collectors.toUnmodifiableList());
    }

    public SingleLineDetailResponse getLine(final Long lineId) {
        return convert(sectionDao.findSectionDetailByLineId(lineId));
    }

    // TODO: 리팩토링
    private SingleLineDetailResponse convert(final List<SectionDetail> sectionDetails) {
        if (sectionDetails.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다.");
        }

        List<Section> sections = SectionConverter.queryResultToDomains(sectionDetails);

        Map<Station, List<Object[]>> map = new HashMap<>();

        for (Section section : sections) {
            map.put(section.getPreviousStation(), new ArrayList<>());
            map.put(section.getNextStation(), new ArrayList<>());
        }

        for (Section section : sections) {
            map.get(section.getPreviousStation()).add(new Object[] {SubwayDirection.UP, section.getNextStation(), section.getDistance()});
            map.get(section.getNextStation()).add(new Object[] {SubwayDirection.DOWN, section.getPreviousStation(), section.getDistance()});
        }

        Deque<Station> deque = new LinkedList<>();
        Set<Station> visited = new HashSet<>();
        moveStation(sections.get(0).getPreviousStation(), deque, map, visited, SubwayDirection.UP);

        return new SingleLineDetailResponse(sectionDetails.get(0).getLineId(), sections.get(0).getLine().getName(), sections.get(0).getLine().getColor(), new Stations(new ArrayList<>(deque)));
    }

    public void moveStation(Station station, Deque<Station> deque, Map<Station, List<Object[]>> map, Set<Station> visited, SubwayDirection direction) {
        visited.add(station);

        getDirection(station, deque, direction);

        for (Object[] object: map.get(station)) {
            moveNextStation(deque, map, visited, object);
        }
    }

    private void getDirection(final Station station, final Deque<Station> deque, final SubwayDirection direction) {
        if (direction == SubwayDirection.UP) {
            deque.addLast(station);
            return;
        }
        deque.addFirst(station);
    }

    private void moveNextStation(final Deque<Station> deque, final Map<Station, List<Object[]>> map, final Set<Station> visited, final Object[] object) {
        if (visited.contains((Station) object[1])) {
            return;
        }

        moveStation((Station) object[1], deque, map, visited, (SubwayDirection) object[0]);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public LineEntity findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
