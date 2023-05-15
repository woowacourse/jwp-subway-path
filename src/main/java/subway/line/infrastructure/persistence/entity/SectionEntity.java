package subway.line.infrastructure.persistence.entity;

import java.util.UUID;
import subway.line.domain.Section;

public class SectionEntity {

    private final Long id;
    private final UUID upStationDomainId;
    private final UUID downStationDomainId;
    private final int distance;
    private final UUID lineDomainId;

    public SectionEntity(final UUID upStationId, final UUID downStationId, final int distance,
                         final UUID lineDomainId) {
        this(null, upStationId, downStationId, distance, lineDomainId);
    }

    public SectionEntity(final Long id,
                         final UUID upStationId,
                         final UUID downStationId,
                         final int distance,
                         final UUID lineDomainId) {
        this.id = id;
        this.upStationDomainId = upStationId;
        this.downStationDomainId = downStationId;
        this.distance = distance;
        this.lineDomainId = lineDomainId;
    }

    public static SectionEntity of(final Section section, final UUID lineDomainId) {
        return new SectionEntity(
                section.up().id(),
                section.down().id(),
                section.distance(),
                lineDomainId);
    }

    public Long id() {
        return id;
    }

    public UUID upStationDomainId() {
        return upStationDomainId;
    }

    public UUID downStationDomainId() {
        return downStationDomainId;
    }

    public int distance() {
        return distance;
    }

    public UUID lineDomainId() {
        return lineDomainId;
    }
}
