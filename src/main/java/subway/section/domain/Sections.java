package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
public class Sections {
    private static final int ADDITIONAL_SECTIONS_NUMBER_OF_BETWEEN_CASE = 3;
    private static final int NUMBER_OF_CONTAIN_MIDDLE_STATION = 2;
    
    private final Set<Section> sections;
    
    public Sections() {
        this(new HashSet<>());
    }
    
    Sections(final Set<Section> sections) {
        this.sections = sections;
    }
    
    public void initAddStation(final String leftAdditional, final String rightAdditional, final long distance) {
        sections.add(new Section(leftAdditional, rightAdditional, distance));
    }
    
    public void addStation(
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        final Set<Section> additionalWithOneself =
                getAdditionalSectionsWithOneself(base, direction, additionalStation, distance);
        
        if (isBetweenCase(additionalWithOneself)) {
            removeIntersection(additionalWithOneself);
        }
        
        sections.addAll(additionalWithOneself);
    }
    
    private Set<Section> getAdditionalSectionsWithOneself(
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        return sections.stream()
                .filter(section -> section.hasStation(base))
                .map(section -> section.getAdditionalSectionsWithOneself(base, direction, additionalStation, distance))
                .max(Comparator.comparingInt(Set::size))
                .orElseThrow(() -> new IllegalArgumentException("추가할 Section이 만들어지지 않았습니다."));
    }
    
    private boolean isBetweenCase(final Set<Section> additionalWithOneself) {
        return additionalWithOneself.size() >= ADDITIONAL_SECTIONS_NUMBER_OF_BETWEEN_CASE;
    }
    
    private void removeIntersection(final Set<Section> additionalWithOneself) {
        final HashSet<Section> sectionsOfRemoveIntersection = new HashSet<>(sections);
        sectionsOfRemoveIntersection.retainAll(additionalWithOneself);
        
        sections.removeAll(sectionsOfRemoveIntersection);
        additionalWithOneself.removeAll(sectionsOfRemoveIntersection);
    }
    
    public void removeStation(final String station) {
        final HashSet<Section> copySections = new HashSet<>(sections);
        final List<Section> sectionsContainedStation = getSectionsOfContainStation(station, copySections);
        sections.removeAll(new HashSet<>(sectionsContainedStation));
        
        if (isMiddleStationCase(sectionsContainedStation)) {
            final Section firstSection = sectionsContainedStation.get(0);
            final Section secondSection = sectionsContainedStation.get(1);
            final Section combinedSection = firstSection.combine(secondSection);
            sections.add(combinedSection);
        }
    }
    
    private List<Section> getSectionsOfContainStation(final String station, final Set<Section> copySections) {
        return copySections.stream()
                .filter(section -> section.hasStation(station))
                .collect(Collectors.toUnmodifiableList());
    }
    
    private boolean isMiddleStationCase(final List<Section> sectionsOfContainStation) {
        return sectionsOfContainStation.size() >= NUMBER_OF_CONTAIN_MIDDLE_STATION;
    }
}
