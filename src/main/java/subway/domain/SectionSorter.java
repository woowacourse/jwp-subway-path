package subway.domain;

import subway.domain.vo.Section;
import subway.domain.vo.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionSorter {
    private static List<Section> line;
    private static List<Section> newLine;
    private static Map<Station, Section> sectionsMap;

    public static List<Section> sorting(List<Section> sections) {
        sectionsMap = makeHashWith(sections);
        Station now = getFirstKey(sectionsMap);
        line = new ArrayList<>();
        newLine = new ArrayList<>();
        while (!sectionsMap.isEmpty()) {
            newLine.add(sectionsMap.get(now));
            now = setNow(now);
        }
        line = merge(newLine, line);
        return copy(line);
    }

    private static Station setNow(Station now) {
        Station pre;
        if (sectionsMap.containsKey(now)) {
            pre = now;
            now = sectionsMap.get(now).getArrival();
            sectionsMap.remove(pre);
            return now;
        }
        now = getFirstKey(sectionsMap);
        line = merge(newLine, line);
        newLine = new ArrayList<>();
        return now;
    }

    private static Station getFirstKey(Map<Station, Section> sectionsMap) {
        return (Station) sectionsMap.keySet().toArray()[0];
    }

    private static Map<Station, Section> makeHashWith(List<Section> sections) {
        Map<Station, Section> sectionMap = new HashMap<>();
        for (Section section : sections) {
            sectionMap.put(section.getDeparture(), section);
        }
        return sectionMap;
    }

    private static List<Section> merge(List<Section> src, List<Section> trg) {
        if (trg == null) {
            return src;
        }
        src.addAll(trg);
        return src;
    }

    private static List<Section> copy(List<Section> input) {
        List<Section> newList = new ArrayList<>();
        for (Section section : input) {
            addNonNullSection(newList, section);
        }
        return newList;
    }

    private static void addNonNullSection(List<Section> newList, Section section) {
        if (section != null) {
            newList.add(section);
        }
    }
}
