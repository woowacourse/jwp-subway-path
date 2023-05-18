package subway.business.service;

import org.springframework.stereotype.Service;
import subway.business.domain.line.LineRepository;
import subway.business.domain.line.Station;
import subway.business.domain.transfer.Transfer;
import subway.business.domain.transfer.TransferRepository;
import subway.business.service.dto.TransferRequest;
import subway.business.service.dto.TransferResponse;

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
}
