package subway.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;

import java.util.List;

@Repository
public class SectionJdbcRepository implements SectionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    new Line(rs.getString("line")),
                    new Station(rs.getString("up_station")),
                    new Station(rs.getString("down_station")),
                    rs.getLong("distance")
            );

    public SectionJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createSection(final String lineName, final List<Section> sections) {
        jdbcTemplate.update("delete from section where line = ?", lineName);
        List<SectionEntity> sectionEntities = SectionEntity.of(sections);

        final BeanPropertySqlParameterSource[] parameterSources = sectionEntities.stream()
            .map(BeanPropertySqlParameterSource::new)
            .toArray(BeanPropertySqlParameterSource[]::new);
        insert.executeBatch(parameterSources);
    }

    @Override
    public List<Section> findAll() {
        return null;
    }

    @Override
    public List<Section> findAllByLineName(final String lineName) {
        String sql = "select * from section where line = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineName);
    }

    @Override
    public void deleteBySection(final Long lineId, final Section section) {
        String sql = "delete from section where lineId = ? and up_station = ? or down_station = ?";
        jdbcTemplate.update(sql, lineId, section.getUpStation(), section.getDownStation());
    }

    @Override
    public Section findIdByUpDown(final String upStation, final String downStation) {
        String sql = "SELECT * from section WHERE up_station = ? and down_station = ?";
        return jdbcTemplate.queryForObject(sql, sectionRowMapper, upStation, downStation);
    }
}
