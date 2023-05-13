package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class Sections {
    private final Set<Section> sections;
    
    public Sections() {
        this.sections = new HashSet<>();
    }
    
    public void initAddStation(final String leftAdditional, final String rightAdditional, final long distance) {
        sections.add(new Section(leftAdditional, rightAdditional, distance));
    }
}
