package subway.service.dto;

public class CreateStationServiceRequest {
    private final String name;

    public CreateStationServiceRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
