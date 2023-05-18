package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SortedStations {
    private final List<Station> stations;

    public static SortedStations from(Sections sections) {
        List<Section> originalSections = sections.getSections();

        if (originalSections.isEmpty()) {
            throw new RuntimeException("존재하지 않는 구간에 대한 정렬 요청이 들어왔습니다");
        }

        Section upEndSection = sections.getUpEndSection();

        List<Station> stations = makeSortedStations(sections, upEndSection);

        return new SortedStations(stations);
    }

    private static List<Station> makeSortedStations(Sections sections, Section upEndSection) {
        List<Station> stations = new ArrayList<>();
        Station upEnd = upEndSection.getPreStation();
        Station pivot = upEndSection.getStation();
        stations.add(upEnd);
        while (!sections.isDownEndStation(pivot)) {
            Section next = sections.getNextSection(pivot);
            stations.add(pivot);
            pivot = next.getStation();
        }
        stations.add(pivot);
        return stations;
    }
}
