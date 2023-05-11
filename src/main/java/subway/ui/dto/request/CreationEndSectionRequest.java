package subway.ui.dto.request;

public class CreationEndSectionRequest {

    private int distance;

    private CreationEndSectionRequest() {
    }

    public CreationEndSectionRequest(final int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
