package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Section;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void insert() {
        // given
        Section lineStation = new Section(1L, 3L, 2L, 3L, 6);

        // when
        Section insertedLineStation = sectionDao.insert(lineStation);

        // then
        assertThat(insertedLineStation).isEqualTo(
                new Section(insertedLineStation.getId(),
                        lineStation.getUpBoundStationId(),
                        lineStation.getDownBoundStationId(),
                        lineStation.getLineId(),
                        lineStation.getDistance()));
    }

    @DisplayName("특정 라인에 해당하는 Section 들을 조회한다.")
    @Test
    void findByLine() {
        // given
        Section firstSection = new Section(1L, 1L, 2L, 1L, 6);
        Section secondSection = new Section(2L, 2L, 3L, 1L, 6);
        Section thirdSection = new Section(3L, 3L, 4L, 1L, 6);


        // when
        Section first = sectionDao.insert(firstSection);
        Section second = sectionDao.insert(secondSection);
        Section third = sectionDao.insert(thirdSection);
        List<Section> lineStations = sectionDao.findByLineId(1L);

        // then
        assertThat(lineStations.containsAll(List.of(first, second, third))).isTrue();
    }

    @DisplayName("Section 을 업데이트 한다.")
    @Test
    void update() {
        // given
        Section firstLineStation = new Section(1L, 2L, 1L, 6);
        Section insertedStation = sectionDao.insert(firstLineStation);
        Section updateStation = new Section(insertedStation.getId(), 1L, 3L, 1L, 6);

        // when
        sectionDao.update(updateStation);
        List<Section> lineStations = sectionDao.findByLineId(1L);

        // then
        assertThat(lineStations.get(0)).isEqualTo(updateStation);
    }

    @DisplayName("Id로 Section 을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Section firstLineStation = new Section(1L, 2L, 1L, 6);
        Section insertedStation = sectionDao.insert(firstLineStation);

        // when
        sectionDao.deleteById(insertedStation.getId());

        // then
        assertThat(sectionDao.findByLineId(1L)).isEmpty();
    }
}
