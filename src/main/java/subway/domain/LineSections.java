package subway.domain;

import subway.entity.SectionEntity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LineSections {
    
    private final List<SectionEntity> sectionEntities;
    private final long upTerminalStationId;
    private final long downTerminalStationId;
    
    private LineSections(final List<SectionEntity> sectionEntities, final long upTerminalStationId,
                         final long downTerminalStationId) {
        this.sectionEntities = sectionEntities;
        this.upTerminalStationId = upTerminalStationId;
        this.downTerminalStationId = downTerminalStationId;
    }
    
    public static LineSections from(final List<SectionEntity> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            return new LineSections(sectionEntities, 0, 0);
        }
        final List<Long> upStationIds = getStationIds(sectionEntities, SectionEntity::getUpStationId);
        final List<Long> downStationIds = getStationIds(sectionEntities, SectionEntity::getDownStationId);
        final Long upTerminalStationId = getTerminalStation(upStationIds, downStationIds::contains);
        final Long downTerminalStationId = getTerminalStation(downStationIds, upStationIds::contains);
        return new LineSections(sectionEntities, upTerminalStationId, downTerminalStationId);
    }
    
    private static List<Long> getStationIds(final List<SectionEntity> sectionEntities,
            final Function<SectionEntity, Long> stationIdGetter) {
        return sectionEntities.stream()
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
        final List<SectionEntity> adjacentSectionEntities = this.getAdjacentSections(stationId);
        return adjacentSectionEntities.size();
    }
    
    public List<SectionEntity> getAdjacentSections(final long stationId) {
        return this.sectionEntities.stream()
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
        return this.sectionEntities.isEmpty();
    }
}
