package subway.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.adapter.out.persistence.dao.LineDao;
import subway.adapter.out.persistence.dao.SectionDao;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.adapter.out.persistence.entity.SectionEntity;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.line.PersistLinePort;
import subway.common.mapper.LineMapper;
import subway.common.mapper.SectionMapper;
import subway.domain.Line;
import subway.domain.LineInfo;
import subway.domain.Section;
import subway.domain.Station;

@Repository
public class LineJdbcRepository implements LoadLinePort, PersistLinePort {

    private final JdbcTemplate jdbcTemplate;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong(1),
                    new Station(rs.getLong(2), rs.getString(3)),
                    new Station(rs.getLong(4), rs.getString(5)),
                    rs.getInt(6)
            );

    public LineJdbcRepository(final JdbcTemplate jdbcTemplate, final LineDao lineDao, final SectionDao sectionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public long create(final LineInfo lineInfo) {
        LineEntity lineEntity = lineDao.insert(LineMapper.toEntity(lineInfo));
        return lineEntity.getId();
    }

    @Override
    public boolean checkExistById(final long lineId) {
        Optional<LineEntity> entity = lineDao.findById(lineId);
        return entity.isPresent();
    }

    @Override
    public Optional<Line> findById(final long lineId) {
        Optional<LineEntity> optionalLineEntity = lineDao.findById(lineId);
        if (optionalLineEntity.isEmpty()) {
            return Optional.empty();
        }
        LineEntity lineEntity = optionalLineEntity.get();

        String sql = "SELECT section.id, up.id, up.name, down.id, down.name, section.distance FROM section "
                + "INNER JOIN station up ON section.up_station_id = up.id "
                + "INNER JOIN station down ON section.down_station_id = down.id "
                + "WHERE section.line_id = ?";

        List<Section> sections = jdbcTemplate.query(sql, sectionRowMapper, lineId);

        Line line = new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
        return Optional.of(line);
    }

    // TODO: n번 쿼리를 날리는 구조 개선....

    @Override
    public List<Line> findAll() {

        List<LineEntity> lines = lineDao.findAll();

        return lines.stream()
                .map(entity -> {
                    Optional<Line> line = findById(entity.getId());
                    if (line.isEmpty()) {
                        throw new IllegalStateException();
                    }
                    return line.get();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findContainingLineIdsByStation(final Station station) {
        List<SectionEntity> sectionEntities = sectionDao.findByStationId(station.getId());
        return sectionEntities.stream()
                .map(SectionEntity::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkExistByName(final String name) {
        Optional<LineEntity> entity = lineDao.findByName(name);
        return entity.isPresent();
    }

    @Override
    public void updateInfo(final long lineId, final LineInfo lineInfo) {
        lineDao.update(LineMapper.toEntity(lineId, lineInfo));
    }

    @Override
    public void updateSections(final Line line) {
        sectionDao.deleteAllByLineId(line.getId());

        List<SectionEntity> sections = line.getSections()
                .stream()
                .map(section -> SectionMapper.toEntity(line.getId(), section))
                .collect(Collectors.toList());

        sectionDao.insertAll(sections);
    }

    @Override
    public void deleteById(final long lineId) {
        lineDao.deleteById(lineId);
    }
}
