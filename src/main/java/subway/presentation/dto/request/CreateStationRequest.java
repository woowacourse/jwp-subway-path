package subway.presentation.dto.request;

public class CreateStationRequest {

    private String name;

    private CreateStationRequest() {
    }

    private CreateStationRequest(final String name) {
        this.name = name;
    }

    public static CreateStationRequest from(final String name) {
        return new CreateStationRequest(name);
    }

    public String getName() {
        return name;
    }
}
