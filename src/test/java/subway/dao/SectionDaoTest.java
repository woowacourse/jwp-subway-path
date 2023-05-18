package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.LineFixture.LINE2_ENTITY;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    @DisplayName("구간 정보를 저장한다.")
    void section_data_insert() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);

        // when
        SectionEntity result = sectionDao.insert(jamsilSeolleung);

        // then
        assertThat(result.getLeftStationId()).isEqualTo(jamsilSeolleung.getLeftStationId());
        assertThat(result.getRightStationId()).isEqualTo(jamsilSeolleung.getRightStationId());
        assertThat(result.getLineId()).isEqualTo(jamsilSeolleung.getLineId());
        assertThat(result.getDistance()).isEqualTo(jamsilSeolleung.getDistance());
    }

    @Test
    @DisplayName("전체 노선 정보를 불러온다.")
    void section_all_data_load() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity gangnam = new StationEntity("강남", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);
        StationEntity insertedGangnam = stationDao.insert(gangnam);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);
        SectionEntity seolleungGangnam = new SectionEntity(insertedSeolleung.getId(), insertedGangnam.getId(), insertedLine.getId(), 10);
        SectionEntity insertedJamsilSeolleung = sectionDao.insert(jamsilSeolleung);
        SectionEntity insertedSeolleungGangnam = sectionDao.insert(seolleungGangnam);

        List<SectionStationEntity> expect = List.of(
                new SectionStationEntity(
                        insertedJamsilSeolleung.getId(),
                        insertedJamsil.getId(),
                        insertedJamsil.getName(),
                        insertedSeolleung.getId(),
                        insertedSeolleung.getName(),
                        insertedLine.getId(),
                        insertedJamsilSeolleung.getDistance()),
                new SectionStationEntity(
                        insertedSeolleungGangnam.getId(),
                        insertedSeolleung.getId(),
                        insertedSeolleung.getName(),
                        insertedGangnam.getId(),
                        insertedGangnam.getName(),
                        insertedLine.getId(), insertedSeolleungGangnam.getDistance())
        );

        // when
        List<SectionStationEntity> result = sectionDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);

    }


    @Test
    @DisplayName("해당 노선의 구간 정보를 불러온다.")
    void section_data_load_by_line_id() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);
        SectionEntity insertedJamsilSeolleung = sectionDao.insert(jamsilSeolleung);

        // when
        List<SectionStationEntity> result = sectionDao.findByLineId(insertedLine.getId());

        // then
        assertThat(result.get(0).getLeftStationId()).isEqualTo(insertedJamsilSeolleung.getLeftStationId());
        assertThat(result.get(0).getLeftStationName()).isEqualTo(insertedJamsil.getName());
        assertThat(result.get(0).getRightStationId()).isEqualTo(insertedJamsilSeolleung.getRightStationId());
        assertThat(result.get(0).getRightStationName()).isEqualTo(insertedSeolleung.getName());
        assertThat(result.get(0).getDistance()).isEqualTo(insertedJamsilSeolleung.getDistance());
    }

    @Test
    @DisplayName("여러 구간을 한번에 삭제한다.")
    void delete_multi_section() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity gangnam = new StationEntity("강남", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);
        StationEntity insertedGangnam = stationDao.insert(gangnam);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);
        SectionEntity seolleungGangNam = new SectionEntity(insertedSeolleung.getId(), insertedGangnam.getId(), insertedLine.getId(), 10);
        SectionEntity insertedJamsilSeolleung = sectionDao.insert(jamsilSeolleung);
        SectionEntity insertedSeolleungGangNam = sectionDao.insert(seolleungGangNam);

        // when
        sectionDao.deleteBothById(List.of(insertedJamsilSeolleung.getId(), insertedSeolleungGangNam.getId()));
        List<SectionStationEntity> result = sectionDao.findByLineId(insertedLine.getId());

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("여러개의 구간을 저장한다.")
    void insert_sections() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity gangnam = new StationEntity("강남", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);
        StationEntity insertedGangnam = stationDao.insert(gangnam);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);
        SectionEntity seolleungGangNam = new SectionEntity(insertedSeolleung.getId(), insertedGangnam.getId(), insertedLine.getId(), 10);

        // when
        sectionDao.insertBoth(List.of(jamsilSeolleung, seolleungGangNam));
        List<SectionStationEntity> result = sectionDao.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간을 id로 삭제한다.")
    void delete_section_by_id() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);
        SectionEntity insertedJamsilSeolleung = sectionDao.insert(jamsilSeolleung);

        // when
        sectionDao.deleteById(insertedJamsilSeolleung.getId());
        List<SectionStationEntity> result = sectionDao.findAll();

        // then
        assertThat(result.size()).isEqualTo(0);
    }
}
