package subway.domain;

import java.util.List;
import java.util.function.Function;
import subway.exception.InvalidInputException;

public final class AdjacentSection {
    
    private final Section section;
    private final Direction direction;
    
    private AdjacentSection(final Section section, final Direction direction) {
        this.section = section;
        this.direction = direction;
    }
    
    public static AdjacentSection filter(final List<Section> sections, final long baseStationId,
            final Direction direction) {
        if (direction.isDown()) {
            final Section tempSection = getSectionInDirection(sections, Section::getUpStationId, baseStationId);
            return new AdjacentSection(tempSection, direction);
        }
        final Section tempSection = getSectionInDirection(sections, Section::getDownStationId, baseStationId);
        return new AdjacentSection(tempSection, direction);
    }
    
    private static Section getSectionInDirection(final List<Section> sections, final Function<Section, Long> function,
            final long baseStationId) {
        return sections.stream()
                .filter(section1 -> function.apply(section1) == baseStationId)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("좌우 구역이 존재하지 않습니다."));
    }
    
    public void validate(final int distance) {
        if (this.section.getDistance() < distance) {
            throw new InvalidInputException("거리가 기존 구역 거리를 초과했습니다.");
        }
    }
    
    public List<Section> splitTwoSections(final long newSectionId, final int distance, final long lineId) {
        if (this.direction.isDown()) {
            final Section newSection1 = new Section(lineId, this.section.getUpStationId(), newSectionId, distance);
            final Section newSection2 = new Section(lineId, newSectionId, this.section.getDownStationId(),
                    this.section.getDistance() - distance);
            return List.of(newSection1, newSection2);
        }
        final Section newSection1 = new Section(lineId, newSectionId, this.section.getDownStationId(), distance);
        final Section newSection2 = new Section(lineId, this.section.getUpStationId(), newSectionId,
                this.section.getDistance() - distance);
        return List.of(newSection1, newSection2);
    }
    
    public Section getSection() {
        return this.section;
    }
}
