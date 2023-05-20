package subway.domain.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import subway.domain.Section;

public class ChangesByDeletion {

    private final List<Section> addedSections;
    private final List<Section> deletedSections;

    public ChangesByDeletion(List<Section> addedSections, List<Section> deletedSections) {
        this.addedSections = addedSections;
        this.deletedSections = deletedSections;
    }

    public ChangesByDeletion combine(ChangesByDeletion changes) {
        return new ChangesByDeletion(
            Stream.of(addedSections, changes.getAddedSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()),
            Stream.of(deletedSections, changes.getDeletedSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

    }

    public List<Section> getAddedSections() {
        return addedSections;
    }

    public List<Section> getDeletedSections() {
        return deletedSections;
    }
}
