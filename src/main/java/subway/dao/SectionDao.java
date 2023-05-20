package subway.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Station;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {
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

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createSection(final String lineName, final List<Section> sections) {
        jdbcTemplate.update("DELETE FROM section WHERE line = ?", lineName);
        List<SectionEntity> sectionEntities = SectionEntity.of(sections);

        final BeanPropertySqlParameterSource[] parameterSources = sectionEntities.stream()
            .map(BeanPropertySqlParameterSource::new)
            .toArray(BeanPropertySqlParameterSource[]::new);
        insert.executeBatch(parameterSources);
    }

    public List<Section> findAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    public List<Section> findAllByLineName(final String lineName) {
        String sql = "SELECT * FROM section WHERE line = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineName);
    }

    public void deleteBySection(final String lineName, final String upStation, final String downStation) {
        String sql = "DELETE FROM section WHERE line = ? AND up_station = ? AND down_station = ?";
        jdbcTemplate.update(sql, lineName, upStation, downStation);
    }

    public Section findIdByUpDown(final String upStation, final String downStation) {
        String sql = "SELECT * FROM section WHERE up_station = ? AND down_station = ?";
        return jdbcTemplate.queryForObject(sql, sectionRowMapper, upStation, downStation);
    }
}
