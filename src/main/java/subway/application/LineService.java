package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

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
        LineEntity persistLine = lineDao.insert(new LineEntity(request.getName(), request.getColor()));
        return persistLine.getId();
    }

    public void saveStationInLine(final Long lineId, final StationRegistrationRequest stationRegistrationRequest) {
        // 예외: 2개의 역이 동일한 역인 경우
        if (Objects.equals(stationRegistrationRequest.getUpStationId(), stationRegistrationRequest.getDownStationId())) {
            throw new IllegalArgumentException("구간은 서로 다른 역이여야 합니다.");
        }

        // 예외: sectionRequest에 들어온 호선이 존재하지 않는 경우
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 호선이 존재하지 않습니다."));

        // 예외: sectionRequest에 들어온 2개의 역이 모두 Station에 존재하는 역인지 검증
        stationDao.findById(stationRegistrationRequest.getUpStationId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 상행역이 존재하지 않습니다."));
        stationDao.findById(stationRegistrationRequest.getDownStationId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 하행역이 존재하지 않습니다."));

        // 예외: sectionRequest에 들어온 2개의 역이 이미 구간이 있는지 검증
        sectionDao.findByStationIds(stationRegistrationRequest.getUpStationId(), stationRegistrationRequest.getDownStationId())
                .ifPresent(section -> {
                    throw new IllegalArgumentException("이미 존재하는 구간입니다.");
                });

        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        Map<Long, Station> stationMap = makeStationMap(stationDao.findAll());
        Station upStation = stationMap.get(stationRegistrationRequest.getUpStationId());
        Station downStation = stationMap.get(stationRegistrationRequest.getDownStationId());
        List<Section> sections = toDomain(sectionEntities, stationMap);

        Line line = new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections));
        Section section = new Section(upStation, downStation, stationRegistrationRequest.getDistance());
        line.add(section);
        sync(lineId, line.getSections()); // TODO: 라인도 삭제해야하는가?
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
        return lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 ID가 없습니다."));
    }

    public List<LineStationResponse> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        List<StationEntity> stationEntities = stationDao.findAll();

        Map<Long, List<SectionEntity>> sectionsByLine = separateByLine(sectionEntities);

        return lineEntities.stream()
                .map(lineEntity -> getLineStationResponse(lineEntity, stationEntities, sectionsByLine.getOrDefault(lineEntity.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private Map<Long, List<SectionEntity>> separateByLine(final List<SectionEntity> sections) {
        return sections.stream()
                    .collect(Collectors.groupingBy(SectionEntity::getLineId));
    }

    public LineStationResponse findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 ID가 없습니다."));
        List<StationEntity> stationEntities = stationDao.findAll();
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        return getLineStationResponse(lineEntity, stationEntities, sectionEntities);
    }

    private LineStationResponse getLineStationResponse(final LineEntity lineEntity, final List<StationEntity> stationEntities, final List<SectionEntity> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            return LineStationResponse.from(LineResponse.of(lineEntity), Collections.emptyList());
        }

        Map<Long, Station> stationMap = makeStationMap(stationEntities);
        Map<Long, Section> sectionMap = makeSectionMap(sectionEntities, stationMap);

        List<StationResponse> stationResponses = sortStations(sectionMap);
        return LineStationResponse.from(LineResponse.of(lineEntity), stationResponses);
    }

    private List<StationResponse> sortStations(final Map<Long, Section> sectionMap) {
        List<StationResponse> stationResponses = new ArrayList<>();
        Section section = sectionMap.get(null);
        while (!section.isDownFinalStation()) { // 하행 종점일 때까지 반복
            Long nextStationId = section.getDownStation().getId();
            Station nextStation = sectionMap.get(nextStationId).getUpStation();
            stationResponses.add(StationResponse.of(nextStation));
            section = sectionMap.get(nextStationId);
        }
        return stationResponses;
    }

    public void update(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        List<StationEntity> stationEntities = stationDao.findAll();

        Map<Long, Station> stationMap = makeStationMap(stationEntities);
        List<Section> sections = toDomain(sectionEntities, stationMap);

        LineEntity lineEntity = findLineById(lineId);
        Line line = new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections));

        List<SectionEntity> deleteSectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);
        List<Section> deleteSections = toDomain(deleteSectionEntities, stationMap);
        if (deleteSections.isEmpty()) {
            throw new NoSuchElementException("삭제할 구간이 존재하지 않습니다.");
        }

        line.remove(new Sections(deleteSections), stationMap.get(stationId));
        sync(lineId, line.getSections());
    }

    private void sync(final Long lineId, final List<Section> sections) {
        sectionDao.deleteByLineId(lineId);
        sectionDao.insertAll(
                sections.stream()
                        .map(section -> new SectionEntity(
                                section.getId(),
                                lineId,
                                section.getUpStation().getId(),
                                section.getDownStation().getId(),
                                section.getDistance()
                        )).collect(Collectors.toList())
        );
    }

    private List<Section> toDomain(final List<SectionEntity> sectionEntities, final Map<Long, Station> stationMap) {
        return sectionEntities.stream()
                .map(sectionEntity -> {
                    Station up = stationMap.getOrDefault(sectionEntity.getUpStationId(), Station.empty());
                    Station down = stationMap.getOrDefault(sectionEntity.getDownStationId(), Station.empty());
                    return new Section(sectionEntity.getId(), up, down, sectionEntity.getDistance());
                }).collect(Collectors.toList());
    }

    private Map<Long, Station> makeStationMap(final List<StationEntity> stationDao) {
        return stationDao.stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        station -> new Station(station.getId(), station.getName())
                ));
    }

    private Map<Long, Section> makeSectionMap(final List<SectionEntity> sectionEntities, final Map<Long, Station> stationMap) {
        return sectionEntities.stream()
                .collect(Collectors.toMap(
                        SectionEntity::getUpStationId,
                        sectionEntity -> new Section(
                                sectionEntity.getId(),
                                stationMap.get(sectionEntity.getUpStationId()),
                                stationMap.get(sectionEntity.getDownStationId()),
                                sectionEntity.getDistance())
                ));
    }
}
