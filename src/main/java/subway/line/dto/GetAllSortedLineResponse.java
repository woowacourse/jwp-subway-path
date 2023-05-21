package subway.line.dto;

import java.util.List;

public class GetAllSortedLineResponse {
    private final List<GetSortedLineResponse> allSortedLines;
    
    public GetAllSortedLineResponse(final List<GetSortedLineResponse> allSortedLines) {
        this.allSortedLines = allSortedLines;
    }
    
    public List<GetSortedLineResponse> getAllSortedLines() {
        return allSortedLines;
    }
    
    @Override
    public String toString() {
        return "GetAllSortedLineResponse{" +
                "allSortedLines=" + allSortedLines +
                '}';
    }
}
