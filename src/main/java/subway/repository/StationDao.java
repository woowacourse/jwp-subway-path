package subway.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;
import subway.exception.DuplicatedStationNameException;
import subway.exception.StationNotFoundException;

import javax.sql.DataSource;
import java.util.List;

import static subway.entity.RowMapperUtil.stationEntityRowMapper;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(final StationEntity stationEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        try {
            final long id = insertAction.executeAndReturnKey(params).longValue();
            return new StationEntity(id, stationEntity.getName());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedStationNameException();
        }
    }

    public List<StationEntity> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, stationEntityRowMapper);
    }

    public StationEntity findById(final long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, stationEntityRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new StationNotFoundException();
        }
    }

    public Long findIdByName(final String name) {
        final String sql = "SELECT id FROM station WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, name);
        } catch (EmptyResultDataAccessException e) {
            throw new StationNotFoundException();
        }
    }

    public boolean isNameExist(final String name) {
        final String sql = "SELECT COUNT(*) FROM station WHERE name = ?";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != 0;
    }

    public void update(final StationEntity newStationEntity) {
        final String sql = "UPDATE station SET name = ? WHERE id = ?";
        try {
            final int result = jdbcTemplate.update(sql, newStationEntity.getName(), newStationEntity.getId());
            validateUpdateResult(result);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedStationNameException();
        }
    }

    public void deleteById(final long id) {
        final String sql = "DELETE FROM station WHERE id = ?";
        final int result = jdbcTemplate.update(sql, id);
        validateUpdateResult(result);
    }

    private void validateUpdateResult(final int result) {
        if (result == 0) {
            throw new StationNotFoundException();
        }
    }
}
