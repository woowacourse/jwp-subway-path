package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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
        validateInitAddCase();
        sections.add(new Section(leftAdditional, rightAdditional, distance));
    }
    
    private void validateInitAddCase() {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("해당 노선의 구간이 없을때만 최초 등록이 가능합니다.");
        }
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
        final Set<Section> sectionsOfRemoveIntersection = new HashSet<>(sections);
        sectionsOfRemoveIntersection.retainAll(additionalWithOneself);
        
        sections.removeAll(sectionsOfRemoveIntersection);
        additionalWithOneself.removeAll(sectionsOfRemoveIntersection);
    }
    
    public void removeStation(final String station) {
        final Set<Section> sectionsContainedStation = getSectionsOfContainStation(station);
        sections.removeAll(sectionsContainedStation);
        
        if (isMiddleStationCase(sectionsContainedStation)) {
            sections.add(getCombinedSection(sectionsContainedStation));
        }
    }
    
    private Set<Section> getSectionsOfContainStation(final String station) {
        return sections.stream()
                .filter(section -> section.hasStation(station))
                .collect(Collectors.toUnmodifiableSet());
    }
    
    private boolean isMiddleStationCase(final Set<Section> sectionsOfContainStation) {
        return sectionsOfContainStation.size() >= NUMBER_OF_CONTAIN_MIDDLE_STATION;
    }
    
    private Section getCombinedSection(final Set<Section> sectionsContainedStation) {
        return sectionsContainedStation.stream()
                .reduce(Section::combine)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 역이 해당 노선에 존재하지 않습니다."));
    }
}
