package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.EIGHT_LINE;
import static subway.fixture.LineFixture.SECOND_LINE;
import static subway.fixture.StationFixture.GANGNAM;
import static subway.fixture.StationFixture.JAMSIL;
import static subway.fixture.StationFixture.SEOKCHON;
import static subway.fixture.StationFixture.YUKSAM;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
public class SectionDaoWithDummyDataTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    SectionEntity yuksamToJamsilSectionEntity;
    SectionEntity yuksamToJamsilSection;

    SectionEntity gangnamToYuksamSectionEntity;
    SectionEntity gangnamToYuksamSection;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);

        yuksamToJamsilSectionEntity = new SectionEntity(JAMSIL.getId(), YUKSAM.getId(), 10, SECOND_LINE.getId());
        yuksamToJamsilSection = sectionDao.insert(yuksamToJamsilSectionEntity);

        gangnamToYuksamSectionEntity = new SectionEntity(YUKSAM.getId(), GANGNAM.getId(), 4, SECOND_LINE.getId());
        gangnamToYuksamSection = sectionDao.insert(gangnamToYuksamSectionEntity);
    }

    @Test
    void 하행종점_경로_삭제() {
        sectionDao.delete(gangnamToYuksamSection.getId());

        List<SectionEntity> sectionsByLine = sectionDao.findSectionsByLine(SECOND_LINE.getId());
        SectionEntity sectionEntity = sectionsByLine.get(0);
        assertAll(
                () -> assertThat(sectionsByLine).hasSize(1),
                () -> assertThat(sectionEntity.getDownStationId()).isEqualTo(YUKSAM.getId()),
                () -> assertThat(sectionEntity.getUpStationId()).isEqualTo(JAMSIL.getId()),
                () -> assertThat(sectionEntity.getDistance()).isEqualTo(yuksamToJamsilSectionEntity.getDistance()),
                () -> assertThat(sectionsByLine).doesNotContain(gangnamToYuksamSectionEntity)
        );

    }

    @Test
    void 상행종점_경로_삭제() {
        sectionDao.delete(yuksamToJamsilSection.getId());

        List<SectionEntity> sectionsByLine = sectionDao.findSectionsByLine(SECOND_LINE.getId());
        SectionEntity sectionEntity = sectionsByLine.get(0);
        assertAll(
                () -> assertThat(sectionsByLine).hasSize(1),
                () -> assertThat(sectionEntity.getDownStationId()).isEqualTo(GANGNAM.getId()),
                () -> assertThat(sectionEntity.getUpStationId()).isEqualTo(YUKSAM.getId()),
                () -> assertThat(sectionEntity.getDistance()).isEqualTo(gangnamToYuksamSectionEntity.getDistance()),
                () -> assertThat(sectionsByLine).doesNotContain(yuksamToJamsilSectionEntity)
        );
    }

    @Test
        // 잠실은 2호선의 역삼, 8호선의 석촌과 연결되어 있다.
        // 잠실과 연결된 section조회 시 2개가 나와야 한다.
    void 역과_연결된_모든_구간_조회() {
        //given
//        StationEntity savedSeokchon = stationDao.insert(SEOKCHON_NO_ID_ENTITY);

        SectionEntity savedSeokchonToJamsilSectionEntity = new SectionEntity(JAMSIL.getId(), SEOKCHON.getId(), 7, EIGHT_LINE.getId());
        sectionDao.insert(savedSeokchonToJamsilSectionEntity);

        //when
        List<SectionEntity> sectionsByStation = sectionDao.findSectionsByStation(JAMSIL.getId());

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
                        JAMSIL.getId(),
                        SEOKCHON.getId(),
                        YUKSAM.getId()
                ),
                () -> assertThat(lineIds).containsExactlyInAnyOrder(EIGHT_LINE.getId(), SECOND_LINE.getId())
        );

    }

}
