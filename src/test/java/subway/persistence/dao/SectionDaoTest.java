package subway.persistence.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:station_data.sql")
class SectionDaoTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsertForSection;
    private final SimpleJdbcInsert simpleJdbcInsertForLine;

    private SectionDao sectionDao;

    private final RowMapper<SectionEntity> rowMapper = (resultSet, rowNumber) -> new SectionEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getLong("upward_station_id"),
            resultSet.getLong("downward_station_id"),
            resultSet.getInt("distance")
    );

    @Autowired
    SectionDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sectionDao = new SectionDao(namedParameterJdbcTemplate);
        this.simpleJdbcInsertForSection = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("section")
                .usingColumns("line_id", "upward_station_id", "downward_station_id", "distance")
                .usingGeneratedKeyColumns("id");
        this.simpleJdbcInsertForLine = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("line")
                .usingColumns("name", "upward_terminus_id", "downward_terminus_id")
                .usingGeneratedKeyColumns("id");
    }

    @DisplayName("DB에 구간을 삽입한다.")
    @Test
    void shouldInsertSectionWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선", 1L, 2L);
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long lineId = simpleJdbcInsertForLine.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntityToInsert = new SectionEntity(lineId, 1L, 2L, 3);
        sectionDao.insert(sectionEntityToInsert);

        String sql = "SELECT id, line_id, upward_station_id, downward_station_id, distance FROM section WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", lineId);
        SectionEntity actualSectionEntity = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource,
                rowMapper);

        assertAll(
                () -> assertThat(actualSectionEntity.getUpwardStationId())
                        .isEqualTo(sectionEntityToInsert.getUpwardStationId()),
                () -> assertThat(actualSectionEntity.getDownwardStationId())
                        .isEqualTo(sectionEntityToInsert.getDownwardStationId()),
                () -> assertThat(actualSectionEntity.getDistance())
                        .isEqualTo(3)
        );
    }

    @DisplayName("DB에서 특정 노선에 속하는 모든 구간을 가져온다.")
    @Test
    void shouldReadAllSectionsWhenRequestLineId() {
        LineEntity lineEntity = new LineEntity("2호선", 1L, 3L);
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long lineId = simpleJdbcInsertForLine.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntityToInsert1 = new SectionEntity(lineId, 1L, 2L, 3);
        sectionDao.insert(sectionEntityToInsert1);

        SectionEntity sectionEntityToInsert2 = new SectionEntity(lineId, 2L, 3L, 3);
        sectionDao.insert(sectionEntityToInsert2);

        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        assertAll(
                () -> assertThat(sectionEntities).hasSize(2),
                () -> assertThat(sectionEntities.get(0).getUpwardStationId())
                        .isEqualTo(sectionEntityToInsert1.getUpwardStationId()),
                () -> assertThat(sectionEntities.get(1).getDownwardStationId())
                        .isEqualTo(sectionEntityToInsert2.getDownwardStationId())
        );
    }

    @DisplayName("DB에서 특정 노선에 속하는 모든 구간을 삭제한다.")
    @Test
    void shouldDeleteSectionsWhenRequestLineId() {
        LineEntity lineEntity = new LineEntity("2호선", 1L, 3L);
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long lineId = simpleJdbcInsertForLine.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntityToInsert1 = new SectionEntity(lineId, 1L, 2L, 3);
        sectionDao.insert(sectionEntityToInsert1);

        SectionEntity sectionEntityToInsert2 = new SectionEntity(lineId, 2L, 3L, 3);
        sectionDao.insert(sectionEntityToInsert2);

        sectionDao.deleteAllByLineId(lineId);
        String sql = "SELECT id, line_id, upward_station_id, downward_station_id, distance FROM section WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", lineId);
        List<SectionEntity> actualSectionEntities = namedParameterJdbcTemplate.query(sql, sqlParameterSource,
                rowMapper);
        assertThat(actualSectionEntities).isEmpty();
    }
}
