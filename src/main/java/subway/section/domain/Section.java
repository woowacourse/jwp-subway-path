package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.station.domain.Station;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class Section {
    private final Station left;
    private final Station right;
    private final Distance distance;
    
    public Section(final String left, final String right, final long distance) {
        this(new Station(left), new Station(right), new Distance(distance));
    }
    
    public Section(final Station left, final Station right, final Distance distance) {
        this.left = left;
        this.right = right;
        this.distance = distance;
    }
    
    public boolean hasStation(final String station) {
        return Set.of(left, right).contains(new Station(station));
    }
    
    public Set<Section> getAdditionalSectionsWithOneself(
            final String base,
            final Direction direction,
            final String additional,
            final long distance
    ) {
        final Set<Section> additionalSectionsWithOneself = new HashSet<>(Set.of(this));
        additionalSectionsWithOneself.addAll(getAdditionalSections(base, direction, additional, distance));
        return additionalSectionsWithOneself;
    }
    
    private Set<Section> getAdditionalSections(
            final String base,
            final Direction direction,
            final String additional,
            final long distance
    ) {
        if (direction.isLeft() && left.equals(new Station(base))) {
            return createLeftSection(additional, distance);
        }
        
        if (direction.isRight() && right.equals(new Station(base))) {
            return createRightSection(additional, distance);
        }
        
        return createDividedSections(additional, distance);
    }
    
    private Set<Section> createLeftSection(final String leftAdditional, final long additionalDistance) {
        final Section additionalRightSection = new Section(leftAdditional, left.getName(), additionalDistance);
        return Set.of(additionalRightSection);
    }
    
    private Set<Section> createRightSection(final String rightAdditional, final long additionalDistance) {
        final Section additionalLeftSection = new Section(right.getName(), rightAdditional, additionalDistance);
        return Set.of(additionalLeftSection);
    }
    
    private Set<Section> createDividedSections(final String betweenAdditional, final long additionalDistance) {
        validateLength(additionalDistance);
        
        final Section additionalLeftSection = new Section(left.getName(), betweenAdditional, additionalDistance);
        final Section additionalRightSection = new Section(betweenAdditional, right.getName(), this.distance.subtract(additionalDistance));
        return Set.of(additionalLeftSection, additionalRightSection);
    }
    
    private void validateLength(final long additionalDistance) {
        if (this.distance.lessThanOrEqualTo(additionalDistance)) {
            throw new IllegalArgumentException("분리될 Section은 추가될 Section의 거리보다 길어야합니다.");
        }
    }
    
    public Section combine(final Section otherSection) {
        final Distance combineDistance = new Distance(this.distance.add(otherSection.distance));
        if (this.right.equals(otherSection.left)) {
            return new Section(this.left, otherSection.right, combineDistance);
        }
        
        return new Section(otherSection.left, this.right, combineDistance);
    }
}
