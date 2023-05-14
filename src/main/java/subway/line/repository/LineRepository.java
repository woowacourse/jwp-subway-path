package subway.line.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.line.dao.LineDao;
import subway.line.dao.SectionDao;
import subway.line.domain.Line;
import subway.line.domain.Lines;
import subway.line.domain.MiddleSection;
import subway.line.entity.LineEntity;
import subway.line.entity.SectionEntity;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Repository
public class LineRepository {

    private final StationRepository stationRepository;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    @Autowired
    public LineRepository(StationRepository stationRepository, LineDao lineDao, SectionDao sectionDao) {
        this.stationRepository = stationRepository;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public long createLine(Line lineToCreate) {
        lineDao.findIdByName(lineToCreate.getName()).ifPresent(ignored -> {
            throw new IllegalStateException("디버깅: 이미 노선 이름에 해당하는 노선이 존재합니다." + lineToCreate.getName());
        });

        final long lineId = lineDao.insert(toLineEntity(lineToCreate));
        sectionDao.insertSections(toSectionEntities(lineToCreate, lineId));

        return lineId;
    }

    private LineEntity toLineEntity(Line line) {
        return new LineEntity.Builder().name(line.getName()).build();
    }

    public long updateLine(Line line) {
        long lineId = lineDao.findIdByName(line.getName())
                             .orElseThrow(() -> new NoSuchElementException("디버깅: 노선 이름에 해당하는 노선이 없습니다. line이름: " + line.getName()));

        sectionDao.deleteSectionsByLineId(lineId);
        sectionDao.insertSections(toSectionEntities(line, lineId));

        return lineId;
    }

    private List<SectionEntity> toSectionEntities(Line line, long lineId) {
        return line.getSections()
                   .stream()
                   .map(section -> toSectionEntity(section, lineId))
                   .collect(Collectors.toUnmodifiableList());
    }

    private SectionEntity toSectionEntity(MiddleSection section, long lineId) {
        long upstreamId = findStationIdByName(section.getUpstreamName());
        long downstreamId = findStationIdByName(section.getDownstreamName());

        return new SectionEntity.Builder().upstreamId(upstreamId)
                                          .downstreamId(downstreamId)
                                          .distance(section.getDistance())
                                          .lineId(lineId)
                                          .build();
    }

    private Long findStationIdByName(String stationName) {
        return stationRepository.findIdByName(stationName)
                                .orElseThrow(() -> new NoSuchElementException("디버깅: 등록되지 않은 역을 Section으로 등록할 수 없습니다 역 이름: " + stationName));
    }

    public Optional<Line> findLineById(Long lineId) {
        List<SectionEntity> sectionsOfLine = sectionDao.findSectionsByLineId(lineId);

        return lineDao.findLineById(lineId)
                      .map(lineEntity -> toLine(lineEntity.getName(), sectionsOfLine));
    }

    public Lines findAllLines() {
        return lineDao.findAll()
                      .stream()
                      .map(lineEntity -> toLine(lineEntity.getName(), sectionDao.findSectionsByLineId(lineEntity.getId())))
                      .collect(collectingAndThen(toList(), Lines::new));
    }

    private Line toLine(String lineName, List<SectionEntity> sectionsOfLineName) {
        return sectionsOfLineName.stream()
                                 .map(this::toMiddleSection)
                                 .collect(collectingAndThen(
                                         toList(),
                                         (sections) -> new Line(lineName, sections)
                                 ));
    }

    private MiddleSection toMiddleSection(SectionEntity sectionEntity) {
        Station upstream = findStationById(sectionEntity.getUpstreamId());
        Station downstream = findStationById(sectionEntity.getDownstreamId());

        return new MiddleSection(upstream, downstream, sectionEntity.getDistance());
    }

    private Station findStationById(long stationId) {
        return stationRepository.findStationById(stationId)
                                .orElseThrow(() -> new NoSuchElementException("디버깅: Section에 존재하지만 Station 테이블에 없는 id입니다. id: " + stationId));
    }

    public void deleteLine(Line lineToDelete) {
        lineDao.deleteLineByName(lineToDelete.getName());
    }
}
