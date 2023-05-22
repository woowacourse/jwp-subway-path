package subway.application.core.domain;

import subway.application.core.exception.CircularRouteException;
import subway.application.core.exception.RouteNotConnectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteMap {

    private final List<Section> routeMap;

    public RouteMap(List<Section> sections) {
        this.routeMap = alignedRouteOf(sections);
    }

    private List<Section> alignedRouteOf(List<Section> sections) {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return alignSections(sections);
    }

    private List<Section> alignSections(List<Section> sections) {
        List<Section> temporarySections = new ArrayList<>();
        Section indexSection = findFirstSection(sections);
        while (canMove(sections, indexSection) && !isInnerCircle(sections, temporarySections)) {
            temporarySections.add(indexSection);
            indexSection = findNext(sections, indexSection);
        }
        temporarySections.add(indexSection);
        validate(sections, temporarySections);
        return temporarySections;
    }

    private Section findFirstSection(List<Section> sections) {
        Station firstStation = getEndPoints(sections).stream()
                .findAny()
                .orElseThrow(CircularRouteException::new);

        return sections.stream()
                .filter(section -> section.hasUpBound(firstStation))
                .findAny()
                .orElseThrow();
    }

    private List<Station> getEndPoints(List<Section> sections) {
        List<Station> allUpBounds = sections.stream()
                .map(Section::getUpBound)
                .collect(Collectors.toList());

        List<Station> allDownBounds = sections.stream()
                .map(Section::getDownBound)
                .collect(Collectors.toList());

        allUpBounds.removeAll(allDownBounds);
        return allUpBounds;
    }

    private boolean canMove(List<Section> sections, Section targetSection) {
        return sections.stream()
                .anyMatch(section -> section.getUpBound().equals(targetSection.getDownBound()));
    }

    private boolean isInnerCircle(List<Section> originalSections, List<Section> temporarySections) {
        return originalSections.size() < temporarySections.size();
    }

    private Section findNext(List<Section> sections, Section targetSection) {
        return sections.stream()
                .filter(section -> section.getUpBound().equals(targetSection.getDownBound()))
                .findAny()
                .orElseThrow();
    }

    private void validate(List<Section> originalSections, List<Section> temporarySections) {
        if (originalSections.size() < temporarySections.size()) {
            throw new CircularRouteException();
        }
        if (temporarySections.size() < originalSections.size()) {
            throw new RouteNotConnectedException();
        }
    }

    public List<Station> stations() {
        Set<Station> alignedStations = new HashSet<>();
        routeMap.forEach(section -> {
            alignedStations.add(section.getUpBound());
            alignedStations.add(section.getDownBound());
        });
        return alignedStations.stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Section> values() {
        return Collections.unmodifiableList(routeMap);
    }
}
