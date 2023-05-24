package subway.line.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.SectionsSorter;
import subway.section.persistence.SectionDao;
import subway.section.persistence.SectionEntity;
import subway.station.domain.Station;

@Repository
public class H2LineRepository implements LineRepository {

  private final LineDao lineDao;
  private final SectionDao sectionDao;

  public H2LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
    this.lineDao = lineDao;
    this.sectionDao = sectionDao;
  }

  @Override
  public Line createLine(final String name) {
    final Optional<LineEntity> lineEntity = lineDao.findByName(name);
    if (lineEntity.isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
    }
    final Line line = new Line(name);
    final Long lineId = lineDao.insert(new LineEntity(line.getLineName()));

    return new Line(lineId, line.getLineName());
  }

  @Override
  public Line findById(final long lineId) {
    final LineEntity lineEntity = lineDao.findById(lineId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선 이름입니다."));
    final List<Section> sortedSections = getSortedSections(lineId);

    return new Line(lineEntity.getId(), lineEntity.getLineName(), Sections.values(sortedSections));
  }

  private List<Section> getSortedSections(long lineId) {
    final SectionsSorter sectionsSorter = SectionsSorter.use(mapToSections(lineId));
    return sectionsSorter.getSortedSections();
  }

  private List<Section> mapToSections(final Long lineId) {
    return sectionDao.findAllByLineId(lineId).stream()
        .map(sectionStationDto -> Section.of(
            new Station(sectionStationDto.getUpStationId(), sectionStationDto.getUpStationName()),
            new Station(sectionStationDto.getDownStationId(), sectionStationDto.getDownStationName())
            , sectionStationDto.getDistance())).collect(Collectors.toList());
  }

  @Override
  public void updateLine(final Line line) {
    sectionDao.deleteAllByLineId(line.getId());

    sectionDao.insertAll(line.getSections().stream()
        .map((section) -> new SectionEntity(
            line.getId(),
            section.getUpStation().getId(),
            section.getDownStation().getId(),
            section.getDistance()))
        .collect(Collectors.toList()));
  }

  @Override
  public List<Line> findAll() {
    final List<LineEntity> lineEntities = lineDao.findAll();

    return lineEntities.stream()
        .map(lineEntity -> new Line(
            lineEntity.getId(),
            lineEntity.getLineName(),
            Sections.values(getSortedSections(lineEntity.getId())))
        )
        .collect(Collectors.toList());
  }

}
