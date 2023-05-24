package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.StationEntity;
import subway.persistence.entity.TransferEntity;

@ActiveProfiles({"test"})
@JdbcTest
class TransferDaoTest {

    private final TransferDao transferDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    TransferDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transferDao = new TransferDao(namedParameterJdbcTemplate);
        this.stationDao = new StationDao(namedParameterJdbcTemplate);
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
    }

    @DisplayName("DB에 환승 정보를 추가 후, 모든 환승 정보를 조회한다.")
    @Test
    void shouldReadAllTransferInformationAfterInsert() {
        LineEntity lineEntity = new LineEntity("2호선", 0);
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        StationEntity stationEntityToInsert1 = new StationEntity(
                lineEntityAfterSave.getId(),
                "봉천"
        );
        long insertedStationId1 = stationDao.insert(stationEntityToInsert1);

        StationEntity stationEntityToInsert2 = new StationEntity(
                lineEntityAfterSave.getId(),
                "서울대입구"
        );
        long insertedStationId2 = stationDao.insert(stationEntityToInsert2);

        TransferEntity transferEntity = new TransferEntity(insertedStationId1, insertedStationId2);
        long savedTransferId = transferDao.insert(transferEntity);

        List<TransferEntity> transferEntities = transferDao.findAll();
        assertThat(transferEntities).hasSize(1);
        assertThat(transferEntities.get(0).getId()).isEqualTo(savedTransferId);
        assertThat(transferEntities.get(0).getFirstStationId()).isEqualTo(insertedStationId1);
        assertThat(transferEntities.get(0).getLastStationId()).isEqualTo(insertedStationId2);
    }
}
