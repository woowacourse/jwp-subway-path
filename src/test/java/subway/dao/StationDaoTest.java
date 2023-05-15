package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.entity.StationEntity;

@JdbcTest
@Import(StationDao.class)
class StationDaoTest {

    @Autowired
    private StationDao stationDao;

    @Test
    @DisplayName("역 정보를 저장한다")
    void insert() {
        // when
        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final Long 저장된_잠실역_아이디 = stationDao.insert(잠실역_엔티티);

        // then
        final Optional<StationEntity> station = stationDao.findById(저장된_잠실역_아이디);
        final StationEntity findStation = station.get();

        assertThat(findStation)
            .extracting(StationEntity::getId, StationEntity::getName)
            .containsExactly(저장된_잠실역_아이디, "잠실역");
    }

    @Test
    @DisplayName("모든 역 정보를 조회한다.")
    void findAll() {
        // given
        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final StationEntity 선릉역_엔티티 = new StationEntity("선릉역");
        final StationEntity 강남역_엔티티 = new StationEntity("강남역");
        final Long 저장된_잠실역_엔티티_아이디 = stationDao.insert(잠실역_엔티티);
        final Long 저장된_선릉역_엔티티_아이디 = stationDao.insert(선릉역_엔티티);
        final Long 저장된_강남역_엔티티_아이디 = stationDao.insert(강남역_엔티티);

        // when
        final List<StationEntity> stationEntities = stationDao.findAll();

        // then
        assertThat(stationEntities).hasSize(3);
        assertThat(stationEntities)
            .extracting(StationEntity::getId, StationEntity::getName)
            .contains(
                tuple(저장된_잠실역_엔티티_아이디, "잠실역"),
                tuple(저장된_선릉역_엔티티_아이디, "선릉역"),
                tuple(저장된_강남역_엔티티_아이디, "강남역"));
    }

    @Test
    @DisplayName("유효한 역 아이디가 주어지면, 역 정보를 조회한다")
    void findById_success() {
        // given
        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final Long 저장된_잠실역_아이디 = stationDao.insert(잠실역_엔티티);

        // when
        final Optional<StationEntity> findStation = stationDao.findById(저장된_잠실역_아이디);

        // then
        final StationEntity station = findStation.get();

        assertThat(station)
            .extracting(StationEntity::getId, StationEntity::getName)
            .containsExactly(저장된_잠실역_아이디, "잠실역");
    }

    @DisplayName("유효하지 않은 역 아이디가 주어지면, 빈 값을 반환한다")
    @Test
    void findById_empty() {
        // when
        final Optional<StationEntity> findStation = stationDao.findById(1L);

        // then
        assertThat(findStation).isEmpty();
    }

    @Test
    @DisplayName("주어진 역 아이디에 해당하는 역 정보를 수정한다.")
    void update() {
        // given
        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final Long 저장된_잠실역_아이디 = stationDao.insert(잠실역_엔티티);

        // when
        final StationEntity 수정_요청_엔티티 = new StationEntity(저장된_잠실역_아이디, "선릉역");
        final int updatedCount = stationDao.update(수정_요청_엔티티);

        // then
        final Optional<StationEntity> station = stationDao.findById(저장된_잠실역_아이디);
        final StationEntity findStation = station.get();

        assertThat(updatedCount).isSameAs(1);
        assertThat(findStation)
            .extracting(StationEntity::getId, StationEntity::getName)
            .containsExactly(저장된_잠실역_아이디, "선릉역");
    }

    @Test
    @DisplayName("주어진 역 아이디에 해당하는 역을 삭제한다.")
    void deleteById() {
        // given
        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        final Long 저장된_잠실역_아이디 = stationDao.insert(잠실역_엔티티);

        // when
        final int deletedCount = stationDao.deleteById(저장된_잠실역_아이디);

        // then
        final Optional<StationEntity> station = stationDao.findById(저장된_잠실역_아이디);

        assertThat(station).isEmpty();
        assertThat(deletedCount).isEqualTo(1);
    }

    @ParameterizedTest(name = "주어진 이름을 가진 역을 존재하면 true를, 아니면 false를 반환한다.")
    @CsvSource(value = {"잠실역:true", "선릉역:false"}, delimiter = ':')
    void existByName(final String name, final boolean expected) {
        // given
        final StationEntity 잠실역_엔티티 = new StationEntity("잠실역");
        stationDao.insert(잠실역_엔티티);

        // expected
        assertThat(stationDao.existByName(name))
            .isSameAs(expected);
    }
}
