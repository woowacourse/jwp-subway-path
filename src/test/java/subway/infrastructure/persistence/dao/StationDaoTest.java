package subway.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.infrastructure.persistence.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationDao 은(는)")
@JdbcTest
@Import(StationDao.class)
class StationDaoTest {

    @Autowired
    private StationDao stationDao;

    private final StationEntity stationEntity = new StationEntity("미역");

    @Test
    void 역을_저장한다() {
        // when
        final Long id = stationDao.save(stationEntity);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void 역을_조회한다() {
        // given
        final Long id = stationDao.save(stationEntity);

        // when & then
        assertThat(stationDao.findById(id)).isPresent();
    }

    @Test
    void 역이_없는_경우() {
        // when & then
        assertThat(stationDao.findById(1L)).isEmpty();
    }
}
