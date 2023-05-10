package subway.domain.dto;

import subway.domain.StationEdge;

public class InsertionResult {

    private final StationEdge insertedEdge;
    private final StationEdge updatedEdge;

    public InsertionResult(StationEdge insertedEdge, StationEdge updatedEdge) {
        this.insertedEdge = insertedEdge;
        this.updatedEdge = updatedEdge;
    }

    public StationEdge getInsertedEdge() {
        return insertedEdge;
    }

    public StationEdge getUpdatedEdge() {
        return updatedEdge;
    }
}
