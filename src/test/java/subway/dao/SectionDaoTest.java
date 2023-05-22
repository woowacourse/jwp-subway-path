package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;

@JdbcTest
@Import({SectionDao.class, LineDao.class, StationDao.class})
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("특정 노선에 대한 구간 정보를 저장한다.")
    void insert() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-red-600", 0);
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);

        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final StationEntity 선릉역_엔티티 = new StationEntity("선릉역");
        final Long 저장된_잠실역_엔티티_아이디 = stationDao.insert(잠실역_엔티티);
        final Long 저장된_선릉역_엔티티_아이디 = stationDao.insert(선릉역_엔티티);

        // when
        final SectionEntity 저장_요청_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 10);
        final long 저장된_구간_아이디 = sectionDao.insert(저장_요청_엔티티);

        // then
        final List<SectionEntity> findSections = getSectionEntities(저장된_구간_아이디);

        assertAll(
            () -> assertThat(findSections).hasSize(1),
            () -> assertThat(findSections.get(0))
                .extracting(SectionEntity::getLineId, SectionEntity::getSourceStationId,
                    SectionEntity::getTargetStationId, SectionEntity::getDistance)
                .containsExactly(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 10));
    }

    @Test
    @DisplayName("특정 노선에 해당하는 특정 출발역 아이디를 기준으로 제거한다.")
    void deleteByLineIdAndSourceStationId() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-red-600", 0);
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);

        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final StationEntity 선릉역_엔티티 = new StationEntity("선릉역");
        final Long 저장된_잠실역_엔티티_아이디 = stationDao.insert(잠실역_엔티티);
        final Long 저장된_선릉역_엔티티_아이디 = stationDao.insert(선릉역_엔티티);

        final SectionEntity 저장_요청_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 10);
        final long 저장된_구간_아이디 = sectionDao.insert(저장_요청_엔티티);

        // when
        final int deletedCount = sectionDao.deleteByLineIdAndSourceStationId(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디);

        // then
        assertThat(deletedCount).isSameAs(1);
        assertThat(getSectionEntities(저장된_구간_아이디)).isEmpty();
    }

    @Test
    @DisplayName("특정 노선에 존재하는 특정 역의 구간 정보 모두를 제거한다.")
    void deleteByLineIdAndStationId() {
        // given
        final LineEntity 이호선_엔티티 = new LineEntity("이호선", "bg-red-600", 0);
        final Long 저장된_이호선_엔티티_아이디 = lineDao.insert(이호선_엔티티);

        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final StationEntity 선릉역_엔티티 = new StationEntity("선릉역");
        final StationEntity 강남역_엔티티 = new StationEntity("강남역");
        final Long 저장된_잠실역_엔티티_아이디 = stationDao.insert(잠실역_엔티티);
        final Long 저장된_선릉역_엔티티_아이디 = stationDao.insert(선릉역_엔티티);
        final Long 저장된_강남역_엔티티_아이디 = stationDao.insert(강남역_엔티티);

        final SectionEntity 잠실_선릉_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_잠실역_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 10);
        final SectionEntity 선릉_강남_엔티티 = new SectionEntity(저장된_이호선_엔티티_아이디, 저장된_선릉역_엔티티_아이디, 저장된_강남역_엔티티_아이디, 10);
        final long 저장된_잠실_선릉_구간_아이디 = sectionDao.insert(잠실_선릉_엔티티);
        final long 저장된_선릉_강남_구간_아이디 = sectionDao.insert(선릉_강남_엔티티);

        // when
        final int deletedCount = sectionDao.deleteByLineIdAndStationId(저장된_이호선_엔티티_아이디, 저장된_선릉역_엔티티_아이디);

        // then
        assertThat(deletedCount).isSameAs(2);
        assertThat(getSectionEntities(저장된_잠실_선릉_구간_아이디)).isEmpty();
        assertThat(getSectionEntities(저장된_선릉_강남_구간_아이디)).isEmpty();
    }

    private List<SectionEntity> getSectionEntities(final long 저장된_구간_아이디) {
        final String sql = "SELECT line_id, source_station_id, target_station_id, distance FROM section WHERE id = ?";
        final List<SectionEntity> findSections = jdbcTemplate.query(sql, (result, count) -> new SectionEntity(
            result.getLong("line_id"),
            result.getLong("source_station_id"),
            result.getLong("target_station_id"),
            result.getInt("distance")), 저장된_구간_아이디);
        return findSections;
    }
}
