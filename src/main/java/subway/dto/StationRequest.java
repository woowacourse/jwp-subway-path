package subway.dto;

import java.util.List;

public class StationRequest {
    private final String name;
    private final List<StationPositionRequest> positions;

    public StationRequest(final String name, final List<StationPositionRequest> positions) {
        this.name = name;
        this.positions = positions;
    }

    public String getName() {
        return name;
    }

    public List<StationPositionRequest> getPositions() {
        return positions;
    }
}
