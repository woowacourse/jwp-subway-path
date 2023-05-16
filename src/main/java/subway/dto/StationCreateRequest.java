package subway.dto;

import java.util.List;

public class StationCreateRequest {
    private final String name;
    private final List<StationPositionRequest> positions;

    public StationCreateRequest(final String name, final List<StationPositionRequest> positions) {
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
