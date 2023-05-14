package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.*;
import subway.entity.Line;
import subway.entity.Section;
import subway.entity.Station;

import java.util.*;
import java.util.stream.Collectors;

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

    public Long save(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return persistLine.getId();
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 ID가 없습니다."));
    }

    public List<LineStationResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();

        Map<Long, List<Section>> sectionsByLine = sortSectionsByLine(sections);

        List<LineStationResponse> lineStationResponses = new ArrayList<>();
        for (Line line : lines) {
            List<StationResponse> stationResponses = linkStationsByLine(sectionsByLine.get(line.getId()), stations);
            lineStationResponses.add(LineStationResponse.from(LineResponse.of(line), stationResponses));
        }
        return lineStationResponses;
    }

    private Map<Long, List<Section>> sortSectionsByLine(final List<Section> sections) {
        Map<Long, List<Section>> sectionsByLine = new HashMap<>();
        for (Section section : sections) {
            List<Section> sectionEntities = sectionsByLine.getOrDefault(section.getLineId(), new ArrayList<>());
            sectionEntities.add(section);
            sectionsByLine.put(section.getLineId(), sectionEntities);
        }
        return sectionsByLine;
    }

    public LineStationResponse findById(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 ID가 없습니다."));

        List<Section> sections = sectionDao.findByLineId(id);
        List<Station> stations = stationDao.findAll();

        List<StationResponse> stationResponses = linkStationsByLine(sections, stations);
        return LineStationResponse.from(LineResponse.of(line), stationResponses);
    }

    private List<StationResponse> linkStationsByLine(final List<Section> sections, final List<Station> stations) {
        if (sections == null) {
            return new ArrayList<>();
        }

        Map<Long, String> stationMap = new HashMap<>();
        stations.forEach(stationEntity -> stationMap.put(stationEntity.getId(), stationEntity.getName()));

        Map<Long, Long> sectionMap = new HashMap<>();
        sections.forEach(section -> sectionMap.put(section.getUpStationId(), section.getDownStationId()));

        return linkStations(stationMap, sectionMap);
    }

    private List<StationResponse> linkStations(final Map<Long, String> stationMap, final Map<Long, Long> sectionMap) {
        List<StationResponse> stationResponses = new ArrayList<>();
        Long nextStationId = sectionMap.get(null);
        while (nextStationId != null) {
            Station station = new Station(nextStationId, stationMap.get(nextStationId));
            stationResponses.add(StationResponse.of(station));
            nextStationId = sectionMap.get(nextStationId);
        }
        return stationResponses;
    }

    public void update(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }

    public Long saveStationInLine(final SectionRequest sectionRequest) {
        // 예외: sectionRequest에 들어온 호선이 존재하지 않는 경우
        if (lineDao.findById(sectionRequest.getLineId()).isEmpty()) {
            throw new NoSuchElementException("해당하는 호선이 존재하지 않습니다.");
        }

        // 예외: sectionRequest에 들어온 2개의 역이 모두 Station에 존재하는 역인지 검증
        if (stationDao.findById(sectionRequest.getUpStationId()).isEmpty() || stationDao.findById(sectionRequest.getStationId()).isEmpty()) {
            throw new NoSuchElementException("해당하는 역이 존재하지 않습니다.");
        }

        // 예외: sectionRequest에 들어온 2개의 역이 구간이 있는지 검증
        if (sectionDao.findByStationIds(sectionRequest.getUpStationId(), sectionRequest.getStationId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 구간입니다.");
        }

        // 1. 노선에 대한 구간이 아예 없는 경우
        List<Section> sectionEntities = sectionDao.findByLineId(sectionRequest.getLineId());
        if (sectionEntities.isEmpty()) {
            sectionDao.insert(new Section(sectionRequest.getLineId(), null, sectionRequest.getUpStationId(), sectionRequest.getDistance()));
            sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), sectionRequest.getDistance()));
            sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getStationId(), null, sectionRequest.getDistance()));
        }

        List<Section> upSections = sectionDao.findByLineIdAndStationId(sectionRequest.getLineId(), sectionRequest.getUpStationId());
        List<Section> downSections = sectionDao.findByLineIdAndStationId(sectionRequest.getLineId(), sectionRequest.getStationId());

        // 예외: 기준이 되는 역이 노선에 존재하지 않는 경우 예외 처리
        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 기준이 되는 역이 존재하지 않습니다.");
        }

        //
        if (downSections.isEmpty()) {
            Section targetSection = upSections.stream()
                    .filter(it -> it.getDownStationId() == null || it.getUpStationId().equals(sectionRequest.getUpStationId()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 구간이 존재하지 않습니다."));

            // 2. 하행 종점에 넣는 경우
            if (targetSection.getDownStationId() == null) {
                sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getStationId(), null, 0));
            }

            // 3. 하행 중간에 넣는 경우
            if (targetSection.getUpStationId().equals(sectionRequest.getUpStationId())) {
                if (targetSection.getDistance() <= sectionRequest.getDistance()) {
                    throw new IllegalArgumentException("현재 구간보다 긴 구간을 추가할 수 없습니다.");
                }
                // AB
                sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getStationId(), targetSection.getDownStationId(), targetSection.getDistance() - sectionRequest.getDistance()));
            }

            sectionDao.deleteBySectionId(targetSection.getId());
        }

        if (upSections.isEmpty()) {
            Section targetSection = downSections.stream()
                    .filter(it -> it.getUpStationId() == null || it.getDownStationId().equals(sectionRequest.getStationId()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 구간이 존재하지 않습니다."));

            // 2. 상행 종점에 넣는 경우
            if (targetSection.getUpStationId() == null) {
                sectionDao.insert(new Section(sectionRequest.getLineId(), null, sectionRequest.getUpStationId(), 0));
            }

            // 3. 상행 중간에 넣는 경우
            if (targetSection.getDownStationId().equals(sectionRequest.getStationId())) {
                if (targetSection.getDistance() <= sectionRequest.getDistance()) {
                    throw new IllegalArgumentException("현재 구간보다 긴 구간을 추가할 수 없습니다.");
                }
                // AB
                sectionDao.insert(new Section(sectionRequest.getLineId(), targetSection.getUpStationId(), sectionRequest.getUpStationId(), targetSection.getDistance() - sectionRequest.getDistance()));
            }

            sectionDao.deleteBySectionId(targetSection.getId());
        }
        return sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), sectionRequest.getDistance()));
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        List<Section> sectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.isEmpty()) {
            throw new NoSuchElementException("해당하는 값이 없습니다.");
        }

        sectionDao.deleteByLineIdAndStationId(lineId, stationId);
        // TODO: 라인에 역이 2개만 있는 경우, 삭제 로직 수정 필요
        if (sectionEntities.size() == 1) {
            linkIfFinalStation(lineId, stationId, sectionEntities.get(0));
            return;
        }
        link(lineId, stationId, sectionEntities);
    }

    private void linkIfFinalStation(final Long lineId, final Long stationId, final Section section) {
        if (sectionDao.findByLineId(lineId).isEmpty()) {
            return;
        }
        if (section.getDownStationId().equals(stationId)) {
            Long upStationId = section.getUpStationId();
            sectionDao.insert(new Section(lineId, upStationId, null, 0));
            return;
        }
        Long downStationId = section.getDownStationId();
        sectionDao.insert(new Section(lineId, null, downStationId, 0));
    }

    private void link(final Long lineId, final Long stationId, final List<Section> sectionEntities) {
        Section previousSection = sectionEntities.get(0);
        Section nextSection = sectionEntities.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStationId().equals(stationId)) {
            Long upStationId = nextSection.getUpStationId();
            Long downStationId = previousSection.getDownStationId();
            sectionDao.insert(new Section(lineId, upStationId, downStationId, newDistance));
            return;
        }
        Long upStationId = previousSection.getUpStationId();
        Long downStationId = nextSection.getDownStationId();
        sectionDao.insert(new Section(lineId, upStationId, downStationId, newDistance));
    }
}
