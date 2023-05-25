package subway.domain;

import subway.entity.SectionDetailEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> allSections;

    public Sections(final List<Section> allSections) {
        this.allSections = allSections;
    }

    public static Sections createByDetailEntity(final List<SectionDetailEntity> detailEntities) {
        final List<Section> sections = detailEntities.stream()
                .map(Section::from)
                .collect(Collectors.toUnmodifiableList());
        return new Sections(sections);
    }

    public Distance getTotalDistance() {
        return allSections.stream()
                .map(Section::getDistance)
                .reduce(Distance.initWithZero(), Distance::add);
    }

    public List<Section> getAllSections() {
        return allSections;
    }
}
