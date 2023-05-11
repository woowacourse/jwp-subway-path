package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
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
        final Line 이호선 = new Line("이호선", "bg-red-600");
        final Line 저장된_이호선 = lineDao.insert(이호선);
        final Long 저장된_이호선_아이디 = 저장된_이호선.getId();

        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");

        final Station 저장된_잠실역 = stationDao.insert(잠실역);
        final Station 저장된_선릉역 = stationDao.insert(선릉역);

        final Long 저장된_시작역_아이디 = 저장된_잠실역.getId();
        final Long 저장된_끝역_아이디 = 저장된_선릉역.getId();

        final SectionEntity 잠실_선릉 = new SectionEntity(저장된_이호선_아이디, 저장된_시작역_아이디, 저장된_끝역_아이디, 10);
        final long savedId = sectionDao.insert(잠실_선릉);

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(잠실_선릉.getLineId());

        // then
        assertAll(
            () -> assertThat(sectionEntities).hasSize(1),
            () -> assertThat(sectionEntities.get(0))
                .extracting("id", "lineId", "sourceStationId", "targetStationId", "distance")
                .containsExactly(savedId, 저장된_이호선_아이디, 저장된_시작역_아이디, 저장된_끝역_아이디, 10));
    }

    @Test
    @DisplayName("노선에 역을 저장한다.")
    void insert() {
        // given
        final Line 이호선 = new Line("이호선", "bg-red-600");
        final Line 저장된_이호선 = lineDao.insert(이호선);
        final Long 저장된_이호선_아이디 = 저장된_이호선.getId();

        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");

        final Station 저장된_잠실역 = stationDao.insert(잠실역);
        final Station 저장된_선릉역 = stationDao.insert(선릉역);

        final Long 저장된_시작역_아이디 = 저장된_잠실역.getId();
        final Long 저장된_끝역_아이디 = 저장된_선릉역.getId();

        final SectionEntity 잠실_선릉 = new SectionEntity(저장된_이호선_아이디, 저장된_시작역_아이디, 저장된_끝역_아이디, 10);

        // when
        final long savedId = sectionDao.insert(잠실_선릉);

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L);
        assertAll(
            () -> assertThat(sectionEntities).hasSize(1),
            () -> assertThat(sectionEntities.get(0))
                .extracting("id", "lineId", "sourceStationId", "targetStationId", "distance")
                .containsExactly(savedId, 저장된_이호선_아이디, 저장된_시작역_아이디, 저장된_끝역_아이디, 10));
    }

    @Test
    @DisplayName("노선 아이디와 출발역 아이디를 받아 제거한다")
    void deleteByLineIdAndSourceStationId() {
        // given
        final Line 이호선 = new Line("이호선", "bg-red-600");
        final Line 저장된_이호선 = lineDao.insert(이호선);
        final Long 저장된_이호선_아이디 = 저장된_이호선.getId();

        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");

        final Station 저장된_잠실역 = stationDao.insert(잠실역);
        final Station 저장된_선릉역 = stationDao.insert(선릉역);

        final Long 저장된_시작역_아이디 = 저장된_잠실역.getId();
        final Long 저장된_끝역_아이디 = 저장된_선릉역.getId();

        final SectionEntity 잠실_선릉 = new SectionEntity(저장된_이호선_아이디, 저장된_시작역_아이디, 저장된_끝역_아이디, 10);
        sectionDao.insert(잠실_선릉);

        // when
        sectionDao.deleteByLineIdAndSourceStationId(저장된_이호선_아이디, 저장된_시작역_아이디);

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(저장된_이호선_아이디);
        assertThat(sectionEntities).isEmpty();
    }
    
    @Test
    @DisplayName("노선 아이디와 역 아이디로 구간 정보를 제거한다.")
    void deleteByLineIdAndStationId() {
        // given
        final Line 이호선 = new Line("이호선", "bg-red-600");
        final Line 저장된_이호선 = lineDao.insert(이호선);
        final Long 저장된_이호선_아이디 = 저장된_이호선.getId();

        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Station 강남역 = new Station("강남역");

        final Station 저장된_잠실역 = stationDao.insert(잠실역);
        final Station 저장된_선릉역 = stationDao.insert(선릉역);
        final Station 저장된_강남역 = stationDao.insert(강남역);

        final Long 저장된_잠실역_아이디 = 저장된_잠실역.getId();
        final Long 저장된_선릉역_아이디 = 저장된_선릉역.getId();
        final Long 저장된_강남역_아이디 = 저장된_강남역.getId();

        final SectionEntity 잠실_선릉 = new SectionEntity(저장된_이호선_아이디, 저장된_잠실역_아이디, 저장된_선릉역_아이디, 10);
        final SectionEntity 선릉_강남 = new SectionEntity(저장된_이호선_아이디, 저장된_선릉역_아이디, 저장된_강남역_아이디, 10);
        sectionDao.insert(잠실_선릉);
        sectionDao.insert(선릉_강남);

        // when
        sectionDao.deleteByLineIdAndStationId(저장된_이호선_아이디, 저장된_선릉역_아이디);

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(저장된_이호선_아이디);
        assertThat(sectionEntities).isEmpty();
    }
}
