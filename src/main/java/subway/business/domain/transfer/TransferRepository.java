package subway.business.domain.transfer;

import java.util.List;

public interface TransferRepository {

    Transfer create(Transfer transfer);

    List<Transfer> findAll();
}
