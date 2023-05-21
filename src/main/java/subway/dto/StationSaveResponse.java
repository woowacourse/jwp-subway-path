package subway.dto;

import java.util.List;

public class StationSaveResponse {

    private final LineDto line;
    private final List<StationSaveDto> savedStations;
    private final List<GeneralSectionDto> savedSections;

    public StationSaveResponse(LineDto line, List<StationSaveDto> savedStations, List<GeneralSectionDto> savedSections) {
        this.line = line;
        this.savedStations = savedStations;
        this.savedSections = savedSections;
    }

    public LineDto getLine() {
        return line;
    }

    public List<StationSaveDto> getSavedStations() {
        return savedStations;
    }

    public List<GeneralSectionDto> getSavedSections() {
        return savedSections;
    }

    @Override
    public String toString() {
        return "StationSaveResponse{" +
                "lineId=" + line +
                ", savedStations=" + savedStations +
                ", savedSectionIds=" + savedSections +
                '}';
    }
}
