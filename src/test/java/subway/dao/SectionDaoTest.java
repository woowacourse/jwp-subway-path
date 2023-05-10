package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.entity.SectionEntity;
import subway.domain.line.Line;
import subway.domain.station.Station;

@JdbcTest
@Import({SectionDao.class, LineDao.class, StationDao.class})
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Test
    @DisplayName("노선의 아이디로 노선에 있는 역 리스트를 조회한다.")
    void findByLineId() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");
        final Line 저장된_신분당선 = lineDao.insert(신분당선);
        final Long 저장된_신분당선_아이디 = 저장된_신분당선.getId();

        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Station 저장된_잠실역 = stationDao.insert(잠실역);
        final Long 저장된_잠실역_아이디 = 저장된_잠실역.getId();
        final Station 저장된_선릉역 = stationDao.insert(선릉역);
        final Long 저장된_선릉역_아이디 = 저장된_선릉역.getId();

        final SectionEntity sectionEntity = new SectionEntity(저장된_신분당선_아이디, 저장된_잠실역_아이디, 저장된_선릉역_아이디, 10, "UPWARD");
        final long savedId = sectionDao.insert(sectionEntity);

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(sectionEntity.getLineId());

        // then
        assertAll(
            () -> assertThat(sectionEntities.size()).isSameAs(1),
            () -> assertThat(sectionEntities.get(0))
                .extracting("id", "lineId", "sourceStationId", "targetStationId", "distance", "sectionType")
                .containsExactly(savedId, 저장된_신분당선_아이디, 저장된_잠실역_아이디, 저장된_선릉역_아이디, 10, "UPWARD"));
    }

    @Test
    @DisplayName("노선에 역을 저장한다.")
    void save() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");
        final Line 저장된_신분당선 = lineDao.insert(신분당선);
        final Long 저장된_신분당선_아이디 = 저장된_신분당선.getId();

        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Station 저장된_잠실역 = stationDao.insert(잠실역);
        final Long 저장된_잠실역_아이디 = 저장된_잠실역.getId();
        final Station 저장된_선릉역 = stationDao.insert(선릉역);
        final Long 저장된_선릉역_아이디 = 저장된_선릉역.getId();

        final SectionEntity sectionEntity = new SectionEntity(저장된_신분당선_아이디, 저장된_잠실역_아이디, 저장된_선릉역_아이디, 10, "UPWARD");

        // when
        final long savedId = sectionDao.insert(sectionEntity);

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L);
        assertAll(
            () -> assertThat(sectionEntities.size()).isSameAs(1),
            () -> assertThat(sectionEntities.get(0))
                .extracting("id", "lineId", "sourceStationId", "targetStationId", "distance", "sectionType")
                .containsExactly(savedId, 저장된_신분당선_아이디, 저장된_잠실역_아이디, 저장된_선릉역_아이디, 10, "UPWARD"));
    }
}
