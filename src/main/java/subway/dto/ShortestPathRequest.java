package subway.dto;

public class ShortestPathRequest {

    private String startName;
    private String destinationName;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(String startName, String destinationName) {
        this.startName = startName;
        this.destinationName = destinationName;
    }

    public String getStartName() {
        return startName;
    }

    public String getDestinationName() {
        return destinationName;
    }
}
