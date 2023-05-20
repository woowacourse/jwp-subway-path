package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.LineEntity;
import subway.persistence.dao.entity.SectionEntity;
import subway.persistence.dao.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineEntityFixture.EIGHT_LINE_NO_ID_ENTITY;
import static subway.fixture.LineEntityFixture.SECOND_LINE_NO_ID_ENTITY;
import static subway.fixture.StationEntityFixture.GANGNAM_NO_ID_ENTITY;
import static subway.fixture.StationEntityFixture.JAMSIL_NO_ID_ENTITY;
import static subway.fixture.StationEntityFixture.SEOKCHON_NO_ID_ENTITY;
import static subway.fixture.StationEntityFixture.YUKSAM_NO_ID_ENTITY;

@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
public class SectionDaoWithDummyDataTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    StationEntity savedJamsil;
    StationEntity savedYuksam;
    StationEntity savedGangnam;

    LineEntity savedSecondLine;
    LineEntity savedEightLine;
    SectionEntity yuksamToJamsilSectionEntity;
    SectionEntity yuksamToJamsilSection;

    SectionEntity gangnamToYuksamSectionEntity;
    SectionEntity gangnamToYuksamSection;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);

        savedJamsil = stationDao.insert(JAMSIL_NO_ID_ENTITY);
        savedYuksam = stationDao.insert(YUKSAM_NO_ID_ENTITY);
        savedGangnam = stationDao.insert(GANGNAM_NO_ID_ENTITY);

        savedSecondLine = lineDao.insert(SECOND_LINE_NO_ID_ENTITY);
        savedEightLine = lineDao.insert(EIGHT_LINE_NO_ID_ENTITY);

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

    @Test
        // 잠실은 2호선의 역삼, 8호선의 석촌과 연결되어 있다.
        // 잠실과 연결된 section조회 시 2개가 나와야 한다.
    void 역과_연결된_모든_구간_조회() {
        //given
        StationEntity savedSeokchon = stationDao.insert(SEOKCHON_NO_ID_ENTITY);

        SectionEntity savedSeokchonToJamsilSectionEntity = new SectionEntity(savedJamsil.getId(), savedSeokchon.getId(), 7, savedEightLine.getId());
        sectionDao.insert(savedSeokchonToJamsilSectionEntity);

        //when
        List<SectionEntity> sectionsByStation = sectionDao.findSectionsByStation(savedJamsil.getId());

        // 조회된 역들의 id값 추출
        List<Long> downStationIds = sectionsByStation.stream()
                .map(SectionEntity::getDownStationId)
                .collect(Collectors.toList());

        List<Long> upStationIds = sectionsByStation.stream()
                .map(SectionEntity::getUpStationId)
                .collect(Collectors.toList());

        downStationIds.addAll(upStationIds);
        List<Long> distinctIds = downStationIds.stream()
                .distinct()
                .collect(Collectors.toList());

        // 조회된 호선 검사
        List<Long> lineIds = sectionsByStation.stream()
                .map(SectionEntity::getLindId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(sectionsByStation).hasSize(2),
                () -> assertThat(distinctIds).containsExactlyInAnyOrder(
                        savedJamsil.getId(),
                        savedSeokchon.getId(),
                        savedYuksam.getId()
                ),
                () -> assertThat(lineIds).containsExactlyInAnyOrder(savedEightLine.getId(), savedSecondLine.getId())
        );

    }

}
