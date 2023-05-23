package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.StationEntity;

@ActiveProfiles({"test"})
@JdbcTest
class StationDaoTest {

    private final StationDao stationDao;
    private final LineDao lineDao;

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    StationDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.stationDao = new StationDao(namedParameterJdbcTemplate);
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
    }

    @DisplayName("DB에 역 정보를 추가 후 ID로 조회한다.")
    @Test
    void shouldInsertStationWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        StationEntity stationEntityToInsert = new StationEntity(
                lineEntityAfterSave.getId(),
                "강남역"
        );
        long insertedStationId = stationDao.insert(stationEntityToInsert);

        StationEntity savedSectionEntity = stationDao.findById(insertedStationId).get();
        assertThat(savedSectionEntity.getName()).isEqualTo(savedSectionEntity.getName());
    }

    @DisplayName("DB로부터 Line ID가 일치하는 모든 Station을 삭제한다.")
    @Test
    void shouldDeleteAllStationsByLineId() {
        LineEntity lineEntity = new LineEntity("2호선");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        StationEntity stationEntityToInsert1 = new StationEntity(
                lineEntityAfterSave.getId(),
                "강남역"
        );
        long insertedStationId1 = stationDao.insert(stationEntityToInsert1);

        StationEntity stationEntityToInsert2 = new StationEntity(
                lineEntityAfterSave.getId(),
                "강남역"
        );
        long insertedStationId2 = stationDao.insert(stationEntityToInsert2);

        stationDao.deleteAllByLineId(lineEntityAfterSave.getId());

        Optional<StationEntity> optionalStation1 = stationDao.findById(insertedStationId1);
        Optional<StationEntity> optionalStation2 = stationDao.findById(insertedStationId2);

        assertAll(
                () -> assertThat(optionalStation1).isEmpty(),
                () -> assertThat(optionalStation2).isEmpty()
        );
    }

    @DisplayName("Line ID와 이름이 일치하는 Station을 조회한다.")
    @Test
    void shouldFindStationWhenInputLineIdAndName() {
        LineEntity lineEntity = new LineEntity("2호선");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        StationEntity stationEntityToInsert = new StationEntity(
                lineEntityAfterSave.getId(),
                "강남역"
        );
        long insertedStationId = stationDao.insert(stationEntityToInsert);

        StationEntity stationEntity = stationDao.findByLineIdAndName(lineEntityAfterSave.getId(), "강남역").get();

        assertThat(stationEntity.getId()).isEqualTo(insertedStationId);
        assertThat(stationEntity.getName()).isEqualTo("강남역");
    }
}
