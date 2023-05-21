package subway.line.dto;

import java.util.List;

public class GetSortedLineResponse {
    private final List<String> sortedStations;
    
    public GetSortedLineResponse(final List<String> sortedStations) {
        this.sortedStations = sortedStations;
    }
    
    public List<String> getSortedStations() {
        return sortedStations;
    }
    
    @Override
    public String toString() {
        return "GetSortedLineResponse{" +
                "sortedStations=" + sortedStations +
                '}';
    }
}
