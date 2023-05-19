package subway.line.dto;

import java.util.List;
import java.util.Objects;

public class GetAllSortedLineResponse {
    private final List<GetSortedLineResponse> allSortedLines;
    
    public GetAllSortedLineResponse(final List<GetSortedLineResponse> allSortedLines) {
        this.allSortedLines = allSortedLines;
    }
    
    public List<GetSortedLineResponse> getAllSortedLines() {
        return allSortedLines;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GetAllSortedLineResponse response = (GetAllSortedLineResponse) o;
        return Objects.equals(allSortedLines, response.allSortedLines);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(allSortedLines);
    }
    
    @Override
    public String toString() {
        return "GetAllSortedLineResponse{" +
                "allSortedLines=" + allSortedLines +
                '}';
    }
}
