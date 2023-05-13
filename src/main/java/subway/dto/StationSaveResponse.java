package subway.dto;

import java.util.List;
import java.util.Objects;

public class StationSaveResponse {

    private final LineDto line;
    private final List<StationDto> savedStations;
    private final List<SectionDto> savedSections;

    public StationSaveResponse(LineDto line, List<StationDto> savedStations, List<SectionDto> savedSections) {
        this.line = line;
        this.savedStations = savedStations;
        this.savedSections = savedSections;
    }

    public LineDto getLine() {
        return line;
    }

    public Long getLineId() {
        return line.getId();
    }

    public List<StationDto> getSavedStations() {
        return savedStations;
    }

    public List<SectionDto> getSavedSections() {
        return savedSections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationSaveResponse that = (StationSaveResponse) o;
        return Objects.equals(line, that.line) && Objects.equals(savedStations, that.savedStations) && Objects.equals(savedSections, that.savedSections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, savedStations, savedSections);
    }

    @Override
    public String toString() {
        return "StationSaveResponse{" +
                "line=" + line +
                ", savedStations=" + savedStations +
                ", savedSections=" + savedSections +
                '}';
    }
}
