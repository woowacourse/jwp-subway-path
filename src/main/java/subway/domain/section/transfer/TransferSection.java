package subway.domain.section.transfer;

import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.general.NearbyStations;

public class TransferSection extends Section {

    private static final int TRANSFER_SECTION_DISTANCE = 0;

    private final Distance distance;

    public TransferSection(final NearbyStations nearbyStations) {
        super(nearbyStations);
        this.distance = new Distance(TRANSFER_SECTION_DISTANCE);
    }

    @Override
    public boolean isSameLineId(Long lineId) {
        throw new IllegalStateException("환승 구간에서는 Line Id를 비교할 수 없습니다.");
    }

    @Override
    public boolean isTransferSection() {
        return true;
    }

    @Override
    public int getDistance() {
        return distance.getDistance();
    }
}
