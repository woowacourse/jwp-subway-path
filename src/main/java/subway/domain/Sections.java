package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sections {

    private final List<Section> sections;

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> unsortedSections) {
        return new Sections(sortSections(unsortedSections));
    }

    public List<Section> getSections() {
        return sections;
    }

    private static List<Section> sortSections(List<Section> sections) {
        final Long firstStationId = getFirstStations(sections);
        List<Section> result = new ArrayList<>();
        Long next = firstStationId;
        for (int i = 0; i < sections.size(); i++) {
            next = addNextSection(sections, result, next);
        }
        return result;
    }

    private static Long getFirstStations(List<Section> sections) {
        final Set<Long> allStationIds = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                .collect(Collectors.toSet());

        final Set<Long> downStationIds = sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toSet());

        final List<Long> stationIds = new ArrayList<>(allStationIds);
        stationIds.removeAll(downStationIds);

        return stationIds.get(0);
    }

    private static Long addNextSection(List<Section> sections, List<Section> result, Long nextStationId) {
        for (Section section : sections) {
            if (section.getUpStationId().equals(nextStationId)) {
                nextStationId = section.getDownStationId();
                result.add(section);
                break;
            }
        }
        return nextStationId;
    }
}
