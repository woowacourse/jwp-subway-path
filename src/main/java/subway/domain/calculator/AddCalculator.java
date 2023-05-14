package subway.domain.calculator;

import subway.domain.section.ContainingSections;
import subway.domain.section.Distance;
import subway.domain.section.Section;

import java.util.ArrayList;
import java.util.List;

public class AddCalculator {

    private final ContainingSections containingSections;

    public AddCalculator(ContainingSections containingSections) {
        this.containingSections = containingSections;
    }

    public Changes addSection(Section newSection) {
        if (containingSections.isEmpty()) {
            return new Changes(new ArrayList<>(), new ArrayList<>(),
                    List.of(newSection.getUpStation(), newSection.getDownStation()), new ArrayList<>(),
                    List.of(newSection), new ArrayList<>());
        }
        if (containingSections.isInsertSectionUpEndCase(newSection)) {
            return new Changes(new ArrayList<>(), new ArrayList<>(),
                    List.of(newSection.getUpStation()), new ArrayList<>(),
                    List.of(newSection), new ArrayList<>());
        }
        if (containingSections.isInsertSectionDownEndCase(newSection)) {
            return new Changes(new ArrayList<>(), new ArrayList<>(),
                    List.of(newSection.getDownStation()), new ArrayList<>(),
                    List.of(newSection), new ArrayList<>());
        }
        Section targetSection = containingSections.getTargetSection(newSection);
        return insertBetweenTargetSection(targetSection, newSection);
    }

    private Changes insertBetweenTargetSection(Section targetSection, Section newSection) {
        if (targetSection.getUpStation() == newSection.getUpStation()) {
            Section modifiedSection = new Section(null, newSection.getDownStation(),
                    targetSection.getDownStation(), subtractDistanceOf(targetSection, newSection));
            return new Changes(new ArrayList<>(), new ArrayList<>(),
                    List.of(newSection.getDownStation()), new ArrayList<>(),
                    List.of(newSection, modifiedSection), List.of(targetSection));
        }
        if (targetSection.getDownStation() == newSection.getDownStation()) {
            Section modifiedSection = new Section(null, targetSection.getUpStation(),
                    newSection.getUpStation(), subtractDistanceOf(targetSection, newSection));
            return new Changes(new ArrayList<>(), new ArrayList<>(),
                    List.of(newSection.getUpStation()), new ArrayList<>(),
                    List.of(modifiedSection, newSection), List.of(targetSection));
        }
        return new Changes(new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }

    private Distance subtractDistanceOf(Section currentSection, Section otherSection) {
        Distance currentDistance = currentSection.getDistance();
        Distance otherSectionDistance = otherSection.getDistance();
        return currentDistance.subtract(otherSectionDistance);
    }
}
