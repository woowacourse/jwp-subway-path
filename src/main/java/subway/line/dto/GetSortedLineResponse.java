package subway.line.dto;

import java.util.List;

public class GetSortedLineResponse {
    private final String lineName;
    private final String lineColor;
    private final List<String> sortedStations;
    
    public GetSortedLineResponse(final String lineName, final String lineColor, final List<String> sortedStations) {
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.sortedStations = sortedStations;
    }
    
    public String getLineName() {
        return lineName;
    }
    
    public String getLineColor() {
        return lineColor;
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
