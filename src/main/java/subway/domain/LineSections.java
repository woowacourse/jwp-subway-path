package subway.domain;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LineSections {
    
    private final List<Section> sections;
    private final long upTerminalStationId;
    private final long downTerminalStationId;
    
    private LineSections(final List<Section> sections, final long upTerminalStationId,
            final long downTerminalStationId) {
        this.sections = sections;
        this.upTerminalStationId = upTerminalStationId;
        this.downTerminalStationId = downTerminalStationId;
    }
    
    public static LineSections from(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new LineSections(sections, 0, 0);
        }
        final List<Long> upStationIds = getStationIds(sections, Section::getUpStationId);
        final List<Long> downStationIds = getStationIds(sections, Section::getDownStationId);
        final Long upTerminalStationId = getTerminalStation(upStationIds, downStationIds::contains);
        final Long downTerminalStationId = getTerminalStation(downStationIds, upStationIds::contains);
        return new LineSections(sections, upTerminalStationId, downTerminalStationId);
    }
    
    private static List<Long> getStationIds(final List<Section> sections,
            final Function<Section, Long> stationIdGetter) {
        return sections.stream()
                .map(stationIdGetter)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private static Long getTerminalStation(final List<Long> stationIds, final Predicate<Long> predicate) {
        return stationIds.stream()
                .filter(predicate.negate())
                .findFirst()
                .orElseThrow();
    }
    
    public boolean hasStation(final long stationId) {
        return this.getAdjacentSectionsSize(stationId) != 0;
    }
    
    public boolean isUpTerminalStation(final long stationId) {
        return this.upTerminalStationId == stationId;
    }
    
    public boolean isDownTerminalStation(final long stationId) {
        return this.downTerminalStationId == stationId;
    }
    
    private int getAdjacentSectionsSize(final long stationId) {
        final List<Section> adjacentSections = this.getAdjacentSections(stationId);
        return adjacentSections.size();
    }
    
    public List<Section> getAdjacentSections(final long stationId) {
        return this.sections.stream()
                .filter(section -> (section.getUpStationId() == stationId) || (section.getDownStationId() == stationId))
                .collect(Collectors.toUnmodifiableList());
    }
    
    public boolean isTerminalStation(final long stationId) {
        return this.isUpTerminalStation(stationId) || this.isDownTerminalStation(stationId);
    }
    
    public long getUpTerminalStationId() {
        return this.upTerminalStationId;
    }
    
    public long getDownTerminalStationId() {
        return this.downTerminalStationId;
    }
    
    public boolean isEmpty() {
        return this.sections.isEmpty();
    }
}
