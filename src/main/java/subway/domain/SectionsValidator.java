package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SectionsValidator {

    private SectionsValidator() {

    }

    public static void validate(final List<Section> sections) {
        if (sections.isEmpty()) {
            return;
        }
        validateDuplicateUpStation(List.copyOf(sections));
        validateAllConnected(new ArrayList<>(sections));
    }

    private static void validateDuplicateUpStation(final List<Section> sections) {
        final long distinctSectionsCount = sections.stream()
            .map(Section::getUpStation)
            .distinct()
            .count();

        if (distinctSectionsCount != sections.size()) {
            throw new IllegalArgumentException("두 개의 하행역으로 향하는 상행역이 존재합니다.");
        }
    }

    private static void validateAllConnected(final List<Section> sections) {
        final Map<String, String> sectionInfo = sections.stream()
            .collect(Collectors.toMap(
                (section) -> section.getUpStation().getName(),
                (section) -> section.getDownStation().getName()
            ));

        final List<String> upStations = new ArrayList<>(sectionInfo.keySet());
        final List<String> downStations = new ArrayList<>(sectionInfo.values());

        upStations.removeAll(downStations);

        if (upStations.size() > 1) {
            throw new IllegalArgumentException("모든 구간이 연결되어 있지 않습니다.( 하행 종점만이 하행역이 없는 상행역이 될 수 있습니다.)");
        }
    }
}
