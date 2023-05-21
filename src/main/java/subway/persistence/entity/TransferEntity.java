package subway.persistence.entity;

public class TransferEntity {
    private final Long id;
    private final Long firstStationId;
    private final Long lastStationId;

    public TransferEntity(Long firstStationId, Long lastStationId) {
        this(null, firstStationId, lastStationId);
    }

    public TransferEntity(Long id, Long firstStationId, Long lastStationId) {
        this.id = id;
        this.firstStationId = firstStationId;
        this.lastStationId = lastStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getLastStationId() {
        return lastStationId;
    }
}
