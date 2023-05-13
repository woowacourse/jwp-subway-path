package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.exception.NoSuchLineException;
import subway.mapper.LineMapper;
import subway.mapper.SectionMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcLineRepository implements LineRepository {

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

    public JdbcLineRepository(final JdbcTemplate jdbcTemplate, final LineDao lineDao, final SectionDao sectionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Line findById(final long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NoSuchLineException(lineId));

        String sql = "SELECT section.id, up.id, up.name, down.id, down.name, section.distance FROM section "
                + "INNER JOIN station up ON section.up_station_id = up.id "
                + "INNER JOIN station down ON section.down_station_id = down.id "
                + "WHERE section.line_id = ?";

        List<Section> sections = jdbcTemplate.query(sql, sectionRowMapper, lineId);

        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
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
    public List<Line> findAll() {

        List<LineEntity> lines = lineDao.findAll();

        return lines.stream()
                .map(entity -> findById(entity.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Line insert(Line line) {
        LineEntity lineEntity = LineMapper.toEntity(line);
        LineEntity saved = lineDao.insert(lineEntity);
        return findById(saved.getId());
    }

    @Override
    public void update(Long id, Line line) {
        LineEntity lineEntity = new LineEntity(id, line.getName(), line.getColor());
        lineDao.update(lineEntity);
    }

    @Override
    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
