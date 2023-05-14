package subway.domain;

import subway.Entity.LineEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Line {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Section> sections;

    private Line(final Long id, final String name, final String color, final List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line of(String name, String color) {
        Section initialSection = Section.ofEmpty();
        List<Section> emptySections = new LinkedList<>();
        emptySections.add(initialSection);
        return new Line(null, name, color, emptySections);
    }
    public static Line of(long id, String name, String color, List<Section> sections) {
        return new Line(id, name, color, sections);
    }

    public static LineEntity toEntity(final Line line) {
        return LineEntity.of(line.id, line.name, line.color);
    }

    public Optional<Section> findSectionContainsStationAsUpward(final Station upwardStation){
       return sections.stream()
               .filter(section -> section.isUpward(upwardStation))
               .findFirst();
    }

    public Optional<Section> findSectionContainsStationAsDownward(final Station downwardStation){
        return sections.stream()
                .filter(section -> section.isDownward(downwardStation))
                .findFirst();
    }

    public void addSection(final Station upwardStation, final Station downwardStation, final int distance) {
        Optional<Section> optionalUpward = findSectionContainsStationAsUpward(upwardStation);
        Optional<Section> optionalDownward = findSectionContainsStationAsDownward(downwardStation);

        if(optionalUpward.isPresent() && optionalDownward.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 구간입니다.");
        }

        if(optionalUpward.isEmpty() && optionalDownward.isEmpty()){
            if(hasAnyStation()){
                throw new IllegalArgumentException("노선에 역을 1개씩 삽입해 주세요.");
            }
            Section emptySection = sections.get(0);
            addSection(emptySection, upwardStation, null, null);

            Section originalSection = findSectionContainsStationAsUpward(upwardStation).orElseThrow(() -> new IllegalStateException());
            addSection(originalSection, downwardStation, distance, null);
            return;
        }

        if(optionalUpward.isEmpty() && optionalDownward.isPresent()){
            Section section = optionalDownward.get();
            System.out.println(section);
            Integer originalDistance = section.getDistance();
            if(originalDistance == null){
                addSection(section, upwardStation, null, distance);
                return;
            }
            addSection(section, upwardStation, originalDistance - distance, distance);
            return;
        }

        if(optionalUpward.isPresent() && optionalDownward.isEmpty()) {
            Section section = optionalUpward.get();
            Integer originalDistance = section.getDistance();
            if(originalDistance == null){
                addSection(section, downwardStation, distance, null);
                return;
            }
            addSection(section, downwardStation, distance, originalDistance - distance);
            return;
        }

        throw new UnsupportedOperationException();
    }

    public void addSection(final Section originalSection, final Station middleStation, final Integer upwardDistance, final Integer downwardDistance) {
        List<Section> splitedSections = originalSection.splitByStation(middleStation, upwardDistance, downwardDistance);
        sections.remove(originalSection);
        sections.addAll(splitedSections);
    }

    public void removeStation(final Station station) {
        Optional<Section> optionalDownward = findSectionContainsStationAsUpward(station);
        Optional<Section> optionalUpward = findSectionContainsStationAsDownward(station);

        if(optionalUpward.isEmpty() && optionalDownward.isEmpty()){
            throw new IllegalArgumentException("노선에 해당 역이 존재하지 않습니다.");
        }

        if(optionalUpward.isPresent() && optionalDownward.isPresent()){
            //두 구간을 하나로 합침
            Section upward = optionalUpward.get();
            Section downward = optionalDownward.get();

            Integer newDistance = null;
            if(!upward.isEmptySection() && !downward.isEmptySection()){
                newDistance = upward.getDistance() + downward.getDistance();
            }
            Section combinedSection = Section.of(upward.getUpward(), downward.getDownward(), newDistance);

            sections.removeAll(List.of(upward, downward));
            sections.add(combinedSection);

            if(getStations().size() == 1){
                removeStation(getStations().get(0));
            }
            return;
        }

        throw new UnsupportedOperationException("무언가 이상...");
    }

    public boolean hasStation(final Station other) {
        return getStations().stream()
                .anyMatch(station -> station.equals(other));
    }

    public boolean hasAnyStation(){
        return getStations().size() != 0;
    }

    public boolean isSameName(Line line) {
        return this.name.equals(line.name);
    }

    public boolean isSameColor(Line line) {
        return this.color.equals(line.color);
    }

    public List<Station> getStationsUpwardToDownward() {
        List<Station> stations = new ArrayList<>();

        Section upwardEnd = getUpwardEndSection();
        Section downwardEnd = getDownwardEndSection();

        Section section = upwardEnd;
        addStationIfNotNull(stations, upwardEnd.getUpward());
        Station station = upwardEnd.getDownward();
        while(!section.equals(downwardEnd)){
            addStationIfNotNull(stations, station);
            section = findNextSectionToDownward(section);
            station = section.getDownward();
        }
        return stations;
    }

    private void addStationIfNotNull(final List<Station> stations, final Station station) {
        if(!station.isEmpty()){
            stations.add(station);
        }
    }

    private Section getUpwardEndSection() {
        return sections.stream()
                .filter(Section::isUpwardEmptySection)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("상행 종점을 찾을 수 없습니다."));
    }

    private Section getDownwardEndSection() {
        return sections.stream()
                .filter(Section::isDownwardEmptySection)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("하행 종점을 찾을 수 없습니다."));
    }

    private Section findNextSectionToDownward(final Section section) {
        return sections.stream()
                .filter(next -> next.isUpward(section.getDownward()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("다음 구간을 찾을 수 없습니다."));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        for(Section section : sections){
            stations.add(section.getUpward());
            stations.add(section.getDownward());
        }
        return stations.stream()
                .distinct()
                .filter(station -> !station.equals(Station.createEmpty()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

}
