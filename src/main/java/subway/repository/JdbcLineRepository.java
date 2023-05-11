package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import subway.mapper.SectionMapper;

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

    @Override
    public void updateSections(final Line line) {
        sectionDao.deleteAllByLineId(line.getId());

        List<SectionEntity> sections = line.getSections()
                .stream()
                .map(section -> SectionMapper.toEntity(line.getId(), section))
                .collect(Collectors.toList());

        sectionDao.insertAll(sections);
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
}
