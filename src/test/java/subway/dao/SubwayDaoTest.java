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
import subway.domain.LineProperty;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class SubwayDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;
    private SubwayDao subwayDao;
    private LinePropertyDao linePropertyDao;

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
        subwayDao = new SubwayDao(jdbcTemplate);
        linePropertyDao = new LinePropertyDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("Line은 저장될 수 있다")
    void test_saveFirst() {
        //given
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";
        LineProperty lineProperty = new LineProperty(1L, "2호선", "red");

        Line line = new Line(new LineProperty(1L, "2호선", "red"), List.of(
                new Section(new Station("푸우"), new Station("테오"), new Distance(1)),
                new Section(new Station("테오"), new Station("제이온"), new Distance(2)),
                new Section(new Station("제이온"), new Station("시카"), new Distance(3))
        ));

        // when

        linePropertyDao.insert(lineProperty);
        subwayDao.save(line);

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
    @DisplayName("id를 통해 해당하는 Line을 찾을 수 있다")
    void test_find() {
        //given
        LineProperty lineProperty = new LineProperty(1L, "2호선", "red");
        Line line = new Line(lineProperty, List.of(
                new Section(new Station("푸우"), new Station("테오"), new Distance(1)),
                new Section(new Station("테오"), new Station("제이온"), new Distance(2)),
                new Section(new Station("제이온"), new Station("시카"), new Distance(3))
        ));

        //when
        stationDao.insert(new Station("푸우"));
        stationDao.insert(new Station("테오"));
        stationDao.insert(new Station("제이온"));
        stationDao.insert(new Station("시카"));

        linePropertyDao.insert(lineProperty);

        subwayDao.save(line);

        Line actualLine = subwayDao.findById(1L);
        List<Section> sections = actualLine.getSections();

        //then
        assertAll(
                () -> assertThat(actualLine.getId()).isEqualTo(1),
                () -> assertThat(actualLine.getName()).isEqualTo("2호선"),
                () -> assertThat(actualLine.getColor()).isEqualTo("red"),
                () -> assertThat(sections).hasSize(3),
                () -> assertThat(sections).anyMatch(section -> section.getUpStation().getName().equals("푸우") &&
                        section.getDownStation().getName().equals("테오") && section.getDistance().value() == 1),
                () -> assertThat(sections).anyMatch(section -> section.getUpStation().getName().equals("테오") &&
                        section.getDownStation().getName().equals("제이온") && section.getDistance().value() == 2),
                () -> assertThat(sections).anyMatch(section -> section.getUpStation().getName().equals("제이온") &&
                        section.getDownStation().getName().equals("시카") && section.getDistance().value() == 3)
        );
    }
}
