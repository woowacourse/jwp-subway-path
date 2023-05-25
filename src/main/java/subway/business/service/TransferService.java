package subway.business.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.domain.line.LineRepository;
import subway.business.domain.line.Station;
import subway.business.domain.transfer.Transfer;
import subway.business.domain.transfer.TransferRepository;
import subway.business.service.dto.TransferRequest;
import subway.business.service.dto.TransferResponse;

@Transactional
@Service
public class TransferService {
    private final LineRepository lineRepository;
    private final TransferRepository transferRepository;

    public TransferService(LineRepository lineRepository, TransferRepository transferRepository) {
        this.lineRepository = lineRepository;
        this.transferRepository = transferRepository;
    }

    public TransferResponse createTransfer(TransferRequest transferRequest) {
        Station station1 = lineRepository.findStationById(transferRequest.getFirstStationId());
        Station station2 = lineRepository.findStationById(transferRequest.getLastStationId());

        Transfer transfer = new Transfer(station1, station2);
        Transfer transferAfterCreate = transferRepository.create(transfer);
        return TransferResponse.from(transferAfterCreate);
    }

    @Transactional(readOnly = true)
    public List<TransferResponse> findAllTransfers() {
        return transferRepository.findAll().stream()
                .map(transfer -> TransferResponse.from(transfer))
                .collect(Collectors.toList());
    }
}
