package subway.presentation.dto.request;

public class CreationStationRequest {

    private String name;

    private CreationStationRequest() {
    }

    private CreationStationRequest(final String name) {
        this.name = name;
    }

    public static CreationStationRequest from(final String name) {
        return new CreationStationRequest(name);
    }

    public String getName() {
        return name;
    }
}
