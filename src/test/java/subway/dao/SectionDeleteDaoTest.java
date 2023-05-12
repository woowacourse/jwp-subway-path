package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.LineFixture;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.service.line.domain.Line;
import subway.service.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.StationFixture.GANGNAM_NO_ID;
import static subway.domain.StationFixture.JAMSIL_NO_ID;
import static subway.domain.StationFixture.YUKSAM_NO_ID;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
public class SectionDeleteDaoTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    Station savedJamsil;
    Station savedYuksam;
    Station savedGangnam;

    Line savedSecondLine;
    SectionEntity yuksamToJamsilSectionEntity;
    SectionEntity yuksamToJamsilSection;

    SectionEntity gangnamToYuksamSectionEntity;
    SectionEntity gangnamToYuksamSection;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);

        savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        savedYuksam = stationDao.insert(YUKSAM_NO_ID);
        savedGangnam = stationDao.insert(GANGNAM_NO_ID);

        savedSecondLine = lineDao.insert(LineFixture.SECOND_LINE_NO_ID);

        yuksamToJamsilSectionEntity = new SectionEntity(savedJamsil.getId(), savedYuksam.getId(), 10, savedSecondLine.getId());
        yuksamToJamsilSection = sectionDao.insert(yuksamToJamsilSectionEntity);

        gangnamToYuksamSectionEntity = new SectionEntity(savedYuksam.getId(), savedGangnam.getId(), 4, savedSecondLine.getId());
        gangnamToYuksamSection = sectionDao.insert(gangnamToYuksamSectionEntity);
    }

    @Test
    void 하행종점_경로_삭제() {
        sectionDao.delete(gangnamToYuksamSection.getId());

        List<SectionEntity> sectionsByLine = sectionDao.findSectionsByLine(savedSecondLine.getId());
        SectionEntity sectionEntity = sectionsByLine.get(0);
        assertAll(
                () -> assertThat(sectionsByLine).hasSize(1),
                () -> assertThat(sectionEntity.getDownStationId()).isEqualTo(savedYuksam.getId()),
                () -> assertThat(sectionEntity.getUpStationId()).isEqualTo(savedJamsil.getId()),
                () -> assertThat(sectionEntity.getDistance()).isEqualTo(yuksamToJamsilSectionEntity.getDistance()),
                () -> assertThat(sectionsByLine).doesNotContain(gangnamToYuksamSectionEntity)
        );

    }

    @Test
    void 상행종점_경로_삭제() {
        sectionDao.delete(yuksamToJamsilSection.getId());

        List<SectionEntity> sectionsByLine = sectionDao.findSectionsByLine(savedSecondLine.getId());
        SectionEntity sectionEntity = sectionsByLine.get(0);
        assertAll(
                () -> assertThat(sectionsByLine).hasSize(1),
                () -> assertThat(sectionEntity.getDownStationId()).isEqualTo(savedGangnam.getId()),
                () -> assertThat(sectionEntity.getUpStationId()).isEqualTo(savedYuksam.getId()),
                () -> assertThat(sectionEntity.getDistance()).isEqualTo(gangnamToYuksamSectionEntity.getDistance()),
                () -> assertThat(sectionsByLine).doesNotContain(yuksamToJamsilSectionEntity)
        );

    }

}
