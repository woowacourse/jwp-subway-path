package subway.persistence.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.business.domain.line.Station;
import subway.business.domain.transfer.Transfer;
import subway.business.domain.transfer.TransferRepository;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.TransferDao;
import subway.persistence.entity.StationEntity;
import subway.persistence.entity.TransferEntity;

@Repository
public class DbTransferRepository implements TransferRepository {
    private final TransferDao transferDao;
    private final StationDao stationDao;

    public DbTransferRepository(TransferDao transferDao, StationDao stationDao) {
        this.transferDao = transferDao;
        this.stationDao = stationDao;
    }

    @Override
    public Transfer create(Transfer transfer) {
        TransferEntity transferEntityToSave = new TransferEntity(
                transfer.getFirstStation().getId(),
                transfer.getLastStation().getId()
        );
        Long savedTransferId = transferDao.insert(transferEntityToSave);
        return new Transfer(savedTransferId, transfer.getFirstStation(), transfer.getLastStation());
    }

    @Override
    public List<Transfer> findAll() {
        return this.transferDao.findAll().stream()
                .map(this::mapTransferEntityToTransfer)
                .collect(Collectors.toList());
    }

    private Transfer mapTransferEntityToTransfer(TransferEntity transferEntity) {
        StationEntity firstStationEntity = stationDao.findById(transferEntity.getFirstStationId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("입력한 ID와 일치하는 Station이 존재하지 않습니다. "
                        + "(입력한 ID : %s)", transferEntity.getFirstStationId())));

        StationEntity lastStationEntity = stationDao.findById(transferEntity.getLastStationId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("입력한 ID와 일치하는 Station이 존재하지 않습니다. "
                        + "(입력한 ID : %s)", transferEntity.getLastStationId())));

        return new Transfer(
                transferEntity.getId(),
                new Station(firstStationEntity.getId(), firstStationEntity.getName()),
                new Station(lastStationEntity.getId(), lastStationEntity.getName())
        );
    }
}
