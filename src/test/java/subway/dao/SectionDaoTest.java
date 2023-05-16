package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    private final RowMapper<SectionEntity> sectionMapper = (rs, cn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getString("up"),
            rs.getString("down"),
            rs.getInt("distance")
    );


    @BeforeEach
    void setting() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("Line은 처음에 저장될 수 있다")
    void test_saveFirst() {
        //given
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";

        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(1L, "푸우", "테오", 1),
                new SectionEntity(1L, "테오", "제이온", 2),
                new SectionEntity(1L, "제이온", "시카", 3)
        );
        sectionDao.save(sectionEntities);

        //when
        List<SectionEntity> actual = jdbcTemplate.query(sectionSql, sectionMapper, 1);

        //then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("푸우") &&
                        sectionEntity.getRight().equals("테오") && sectionEntity.getDistance() == 1),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("테오") &&
                        sectionEntity.getRight().equals("제이온") && sectionEntity.getDistance() == 2),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("제이온") &&
                        sectionEntity.getRight().equals("시카") && sectionEntity.getDistance() == 3)
        );
    }

    @Test
    @DisplayName("Line은 저장될 때마다 기존 정보를 덮어씌운다")
    void save() {
        //given
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";

        List<SectionEntity> origin = List.of(
                new SectionEntity(1L, "푸우", "테오", 1),
                new SectionEntity(1L, "테오", "제이온", 2),
                new SectionEntity(1L, "제이온", "시카", 3)
        );
        List<SectionEntity> update = List.of(
                new SectionEntity(1L, "창원", "잠실", 1),
                new SectionEntity(1L, "잠실", "화성", 2),
                new SectionEntity(1L, "화성", "목성", 3)
        );
        sectionDao.save(origin);
        sectionDao.save(update);

        List<SectionEntity> actual = jdbcTemplate.query(sectionSql, sectionMapper, 1);

        //then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("창원") &&
                        sectionEntity.getRight().equals("잠실") && sectionEntity.getDistance() == 1),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("잠실") &&
                        sectionEntity.getRight().equals("화성") && sectionEntity.getDistance() == 2),
                () -> assertThat(actual).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("화성") &&
                        sectionEntity.getRight().equals("목성") && sectionEntity.getDistance() == 3)
        );
    }

    @Test
    @DisplayName("id를 통해 해당하는 Line을 찾을 수 있다")
    void findById() {
        //given
        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(1L, "푸우", "테오", 1),
                new SectionEntity(1L, "테오", "제이온", 2),
                new SectionEntity(1L, "제이온", "시카", 3)
        );
        sectionDao.save(sectionEntities);


        //when
        final List<SectionEntity> returnEntities = sectionDao.findById(1L);

        //then
        assertAll(
                () -> assertThat(returnEntities).hasSize(3),
                () -> assertThat(returnEntities).allMatch(sectionEntity -> sectionEntity.getLineId() == 1L),
                () -> assertThat(returnEntities).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("푸우") &&
                        sectionEntity.getRight().equals("테오") && sectionEntity.getDistance() == 1),
                () -> assertThat(returnEntities).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("테오") &&
                        sectionEntity.getRight().equals("제이온") && sectionEntity.getDistance() == 2),
                () -> assertThat(returnEntities).anyMatch(sectionEntity -> sectionEntity.getLeft().equals("제이온") &&
                        sectionEntity.getRight().equals("시카") && sectionEntity.getDistance() == 3)
        );
    }
}
