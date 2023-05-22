package subway.application.request;


import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank
    private final String name;

    public StationRequest(final String name) {
        this.name = name;
    }

    private StationRequest() {
        this(null);
    }

    public String getName() {
        return name;
    }
}
