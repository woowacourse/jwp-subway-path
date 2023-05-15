//package subway.domain;
//
//import subway.domain.Line;
//import subway.domain.Sections;
//import subway.domain.Station;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class Route {
//
//    private final Map<Line, Sections> sectionsByLine;
//
//    public Route(final Map<Line, Sections> sectionsByLine) {
//        this.sectionsByLine = sectionsByLine;
//    }
//
//    public void insertLine(final Line line) {
//        sectionsByLine.put(line, new Sections(new ArrayList<>()));
//    }
//
//    public void updateLine(final Line targetLine, final Line updateLine) {
//        validateLineExistence(targetLine);
//        sectionsByLine.put(updateLine, sectionsByLine.get(targetLine));
//        sectionsByLine.remove(targetLine);
//    }
//
//    private void validateLineExistence(final Line line) {
//        if (!sectionsByLine.containsKey(line)) {
//            throw new IllegalArgumentException("해당 호선이 존재하지 않습니다.");
//        }
//    }
//
////    public void insertSection(final Line line, final Station from, final Station to, final int distance) {
////        validateLineExistence(line);
////        final Sections sections = sectionsByLine.get(line);
////
////        if (sections.isEmpty()) {
////            sections.insertInitially(from, to, distance);
////            return;
////        }
////
////        sections.insert(from, to, distance);
////    }
////
////    public void updateStation(final Line line, final Station targetStation, final Station updateStation) {
////        validateLineExistence(line);
////        final Sections sections = sectionsByLine.get(line);
////
////        if (!sections.hasStation(targetStation)) {
////            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
////        }
////
////        sections.updateStation(targetStation, updateStation);
////    }
//
//    public void deleteStation(final Line line, final Station station) {
//        final Sections sections = sectionsByLine.get(line);
//        sections.delete(station);
//    }
//
//    public Map<Line, Sections> getSectionsByLine() {
//        return sectionsByLine;
//    }
//
//    public List<Line> getLines() {
//        return sectionsByLine.keySet().stream().collect(Collectors.toUnmodifiableList());
//    }
//}
