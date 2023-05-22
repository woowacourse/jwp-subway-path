package subway.presentation.dto;

public class JourneyRequest {

    private Long departure;
    private Long terminal;

    public JourneyRequest() {
    }

    public JourneyRequest(Long departure, Long terminal) {
        this.departure = departure;
        this.terminal = terminal;
    }

    public Long getDeparture() {
        return departure;
    }

    public Long getTerminal() {
        return terminal;
    }
}
