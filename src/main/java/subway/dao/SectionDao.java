package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import javax.sql.DataSource;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(final SectionEntity sectionEntity) {
        final String sql =
                "INSERT INTO section(line_id, distance, previous_station_id, next_station_id) VALUES(?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                sectionEntity.getLineId(),
                sectionEntity.getDistance(),
                sectionEntity.getPreviousStationId(),
                sectionEntity.getNextStationId()
        );
    }

}
