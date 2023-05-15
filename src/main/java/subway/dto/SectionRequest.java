package subway.dto;

public class SectionRequest {
    private final String startingStation;
    private final String destinationStation;
    public SectionRequest(String startingStation, String destinationStation) {
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
    }

    public String getStartingStation() {
        return startingStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }
}
