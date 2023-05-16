package subway.business.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LineSectionsSortFactory {

    private static final int FIRST_INDEX = 0;

    public static List<Section> sort(final List<Section> unsortedSections) {
        final Map<Station, Section> sectionsByPreviousStation = extractPreviousStations(unsortedSections);
        final Map<Station, Section> sectionsByNextStation = extractNextStations(unsortedSections);
        final Section standardSection = unsortedSections.get(FIRST_INDEX);
        final List<Section> sortedSections = new ArrayList<>(makeDownside(standardSection, sectionsByNextStation));
        sortedSections.add(standardSection);
        sortedSections.addAll(makeUpside(standardSection, sectionsByPreviousStation));
        return sortedSections;
    }

    private static Map<Station, Section> extractPreviousStations(final List<Section> sections) {
        return sections.stream()
                .collect(Collectors.toUnmodifiableMap(Section::getPreviousStation, section -> section));
    }

    private static Map<Station, Section> extractNextStations(final List<Section> sections) {
        return sections.stream()
                .collect(Collectors.toUnmodifiableMap(Section::getNextStation, section -> section));
    }

    private static List<Section> makeUpside(
            final Section standardSection, final Map<Station, Section> sectionsByPreviousStation) {
        final List<Section> upsideSections = new ArrayList<>();
        Station nextStation = standardSection.getNextStation();
        while (sectionsByPreviousStation.containsKey(nextStation)) {
            final Section nextSection = sectionsByPreviousStation.get(nextStation);
            upsideSections.add(nextSection);
            nextStation = nextSection.getNextStation();
        }
        return upsideSections;
    }

    private static List<Section> makeDownside(
            final Section standardSection, final Map<Station, Section> sectionsByNextStation) {
        final List<Section> downsideSections = new ArrayList<>();
        Station previousStation = standardSection.getPreviousStation();
        while (sectionsByNextStation.containsKey(previousStation)) {
            final Section previousSection = sectionsByNextStation.get(previousStation);
            downsideSections.add(FIRST_INDEX, previousSection);
            previousStation = previousSection.getPreviousStation();
        }
        return downsideSections;
    }
}
