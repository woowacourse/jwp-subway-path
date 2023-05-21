package subway.business.domain.transfer;

import java.util.ArrayList;
import java.util.List;

public class MemoryTransferRepository implements TransferRepository {
    private final List<Transfer> transfers = new ArrayList<>();
    private long serial = 1;


    @Override
    public Transfer create(Transfer transfer) {
        Transfer transferToAdd = new Transfer(serial++, transfer.getFirstStation(), transfer.getLastStation());
        transfers.add(transferToAdd);
        return transferToAdd;
    }

    @Override
    public List<Transfer> findAll() {
        return transfers;
    }
}
