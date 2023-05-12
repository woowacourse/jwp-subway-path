package subway.persistence.dao;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.persistence.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class StationDaoTest extends DaoTest {

    @Test
    void 역을_저장한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        final StationEntity actual = stationDao.insert(stationEntity);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 모든_역을_조회한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        stationDao.insert(stationEntity);

        final List<StationEntity> actual = stationDao.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 역_하나를_조회한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        final StationEntity actual = stationDao.findById(insertedStationEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 역_하나를_삭제한다() {
        final StationEntity stationEntity = StationEntity.from("잠실역");
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        final int actual = assertDoesNotThrow(() -> stationDao.deleteById(insertedStationEntity.getId()));

        assertThat(actual).isOne();
    }
}
