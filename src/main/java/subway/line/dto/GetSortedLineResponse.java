package subway.line.dto;

import java.util.List;
import java.util.Objects;

public class GetSortedLineResponse {
    private final List<String> sortedStations;
    
    public GetSortedLineResponse(final List<String> sortedStations) {
        this.sortedStations = sortedStations;
    }
    
    public List<String> getSortedStations() {
        return sortedStations;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GetSortedLineResponse that = (GetSortedLineResponse) o;
        return Objects.equals(sortedStations, that.sortedStations);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sortedStations);
    }
    
    @Override
    public String toString() {
        return "GetSortedLineResponse{" +
                "sortedStations=" + sortedStations +
                '}';
    }
}
