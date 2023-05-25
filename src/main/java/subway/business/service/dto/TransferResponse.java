package subway.business.service.dto;

import subway.business.domain.transfer.Transfer;

public class TransferResponse {
    private final Long id;
    private final StationDto firstStation;
    private final StationDto lastStation;

    private TransferResponse(Long id, StationDto firstStation, StationDto lastStation) {
        this.id = id;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
    }

    public static TransferResponse from(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                StationDto.from(transfer.getFirstStation()),
                StationDto.from(transfer.getLastStation())
        );
    }

    public Long getId() {
        return id;
    }

    public StationDto getFirstStation() {
        return firstStation;
    }

    public StationDto getLastStation() {
        return lastStation;
    }
}
