package subway.business.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LineSectionsSortFactory {

    private static final int FIRST_INDEX = 0;

    public static List<LineSection> sort(final List<LineSection> unsortedSections) {
        final Map<Station, LineSection> sectionsByPreviousStation = extractPreviousStations(unsortedSections);
        final Map<Station, LineSection> sectionsByNextStation = extractNextStations(unsortedSections);
        final LineSection standardSection = unsortedSections.get(FIRST_INDEX);
        final List<LineSection> sortedSections = new ArrayList<>(makeDownside(standardSection, sectionsByNextStation));
        sortedSections.add(standardSection);
        sortedSections.addAll(makeUpside(standardSection, sectionsByPreviousStation));
        validateSectionsSize(unsortedSections, sortedSections);
        return sortedSections;
    }

    private static Map<Station, LineSection> extractPreviousStations(final List<LineSection> sections) {
        return sections.stream()
                .collect(Collectors.toUnmodifiableMap(LineSection::getPreviousStation, section -> section));
    }

    private static Map<Station, LineSection> extractNextStations(final List<LineSection> sections) {
        return sections.stream()
                .collect(Collectors.toUnmodifiableMap(LineSection::getNextStation, section -> section));
    }

    private static List<LineSection> makeUpside(
            final LineSection standardSection, final Map<Station, LineSection> sectionsByPreviousStation) {
        final List<LineSection> upsideSections = new ArrayList<>();
        Station nextStation = standardSection.getNextStation();
        while (sectionsByPreviousStation.containsKey(nextStation)) {
            final LineSection nextSection = sectionsByPreviousStation.get(nextStation);
            upsideSections.add(nextSection);
            nextStation = nextSection.getNextStation();
        }
        return upsideSections;
    }

    private static List<LineSection> makeDownside(
            final LineSection standardSection, final Map<Station, LineSection> sectionsByNextStation) {
        final List<LineSection> downsideSections = new ArrayList<>();
        Station previousStation = standardSection.getPreviousStation();
        while (sectionsByNextStation.containsKey(previousStation)) {
            final LineSection previousSection = sectionsByNextStation.get(previousStation);
            downsideSections.add(FIRST_INDEX, previousSection);
            previousStation = previousSection.getPreviousStation();
        }
        return downsideSections;
    }

    private static void validateSectionsSize(final List<LineSection> unSortedSections, final List<LineSection> sortedSections) {
        if (unSortedSections.size() != sortedSections.size()) {
            throw new RuntimeException("같은 노선에 순환 구간이나 갈림길이 있습니다.");
        }
    }

}
