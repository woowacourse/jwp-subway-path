package subway.domain.calculator;

import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;
import java.util.Objects;

public class Changes {

    private final Long changedLineId;
    private final List<Line> lineToAdd;
    private final List<Line> lineToRemove;
    private final List<Station> stationsToAdd;
    private final List<Station> stationsToRemove;
    private final List<Section> sectionsToAdd;
    private final List<Section> sectionsToRemove;

    public Changes(Long changedLineId, List<Line> lineToAdd, List<Line> lineToRemove,
                   List<Station> stationsToAdd, List<Station> stationsToRemove,
                   List<Section> sectionsToAdd, List<Section> sectionsToRemove) {
        this.changedLineId = changedLineId;
        this.lineToAdd = lineToAdd;
        this.lineToRemove = lineToRemove;
        this.stationsToAdd = stationsToAdd;
        this.stationsToRemove = stationsToRemove;
        this.sectionsToAdd = sectionsToAdd;
        this.sectionsToRemove = sectionsToRemove;
    }

    public Long getChangedLineId() {
        return changedLineId;
    }

    public List<Line> getLineToAdd() {
        return lineToAdd;
    }

    public List<Line> getLineToRemove() {
        return lineToRemove;
    }

    public List<Station> getStationsToAdd() {
        return stationsToAdd;
    }

    public List<Station> getStationsToRemove() {
        return stationsToRemove;
    }

    public List<Section> getSectionsToAdd() {
        return sectionsToAdd;
    }

    public List<Section> getSectionsToRemove() {
        return sectionsToRemove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Changes changes = (Changes) o;
        return Objects.equals(changedLineId, changes.changedLineId) && Objects.equals(lineToAdd, changes.lineToAdd) && Objects.equals(lineToRemove, changes.lineToRemove) && Objects.equals(stationsToAdd, changes.stationsToAdd) && Objects.equals(stationsToRemove, changes.stationsToRemove) && Objects.equals(sectionsToAdd, changes.sectionsToAdd) && Objects.equals(sectionsToRemove, changes.sectionsToRemove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(changedLineId, lineToAdd, lineToRemove, stationsToAdd, stationsToRemove, sectionsToAdd, sectionsToRemove);
    }

    @Override
    public String toString() {
        return "Changes{" +
                "changedLineId=" + changedLineId +
                ", lineToAdd=" + lineToAdd +
                ", lineToRemove=" + lineToRemove +
                ", stationsToAdd=" + stationsToAdd +
                ", stationsToRemove=" + stationsToRemove +
                ", sectionsToAdd=" + sectionsToAdd +
                ", sectionsToRemove=" + sectionsToRemove +
                '}';
    }
}
