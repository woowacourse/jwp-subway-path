package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upward;
    private final Station downward;
    private final Distance distance;

    private Section(Long id, Station upward, Station downward, Distance distance) {
        validateSameStations(upward, downward);
        this.id = id;
        this.upward = upward;
        this.downward = downward;
        this.distance = distance;
    }

    public static Section of(Long id, Station upward, Station downward, Integer distance) {
        return new Section(id, upward, downward, Distance.from(distance));
    }

    public static Section of(Station upward, Station downward, Integer distance) {
        return new Section(null, upward, downward, Distance.from(distance));
    }

    public static Section ofEmpty() {
        return new Section(null, Station.createEmpty(), Station.createEmpty(), Distance.createEmpty());
    }

    private void validateSameStations(Station upward, Station downward) {
        if(upward.isEmpty() && downward.isEmpty()){
            return;
        }
        if (upward.equals(downward)) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
        }
        if (downward.equals(upward)) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
        }
    }
    public List<Section> splitByStation(final Station middleStation, Integer upwardDistance, Integer downwardDistance){

        /*if(isEmptySection() && this.upward.isEmpty() && this.downward.isEmpty()){
            Section upwardSection = Section.of(
                    this.upward,
                    middleStation,
                    null
            );
            Section downwardSection = Section.of(
                    middleStation,
                    this.downward,
                    null
            );
            return List.of(upwardSection, downwardSection);
        }

        if(isEmptySection() && !this.upward.isEmpty() && this.downward.isEmpty()){
            Section upwardSection = Section.of(
                    this.upward,
                    middleStation,
                    upwardDistance
            );
            Section downwardSection = Section.of(
                    middleStation,
                    this.downward,
                    null
            );
            return List.of(upwardSection, downwardSection);
        }

        if(isEmptySection() && this.upward.isEmpty() && !this.downward.isEmpty()){
            Section upwardSection = Section.of(
                    this.upward,
                    middleStation,
                    null
            );
            Section downwardSection = Section.of(
                    middleStation,
                    this.downward,
                    downwardDistance
            );
            return List.of(upwardSection, downwardSection);
        }*/

        if(!isEmptySection() && !isSameDistance(upwardDistance+downwardDistance)){
            throw new IllegalArgumentException("구간 거리가 보존되지 않습니다.");
        }
        Section upwardSection = Section.of(
                this.upward,
                middleStation,
                upwardDistance
        );
        Section downwardSection = Section.of(
                middleStation,
                this.downward,
                downwardDistance
        );
        return List.of(upwardSection, downwardSection);
    }

    public boolean isUpwardEmptySection() {
        return this.upward.isEmpty() && this.distance.isEmpty();
    }

    public boolean isDownwardEmptySection() {
        return this.downward.isEmpty() && this.distance.isEmpty();
    }

    public boolean isComposed(Station upward, Station downward) {
        return this.upward.equals(upward) && this.downward.equals(downward);
    }

    public boolean isSameDistance(int distance) {
        return this.distance.equals(Distance.from(distance));
    }

    public boolean isEmptySection(){
        return this.distance.equals(Distance.createEmpty());
    }

    public Long getId() {
        return id;
    }

    public Station getUpward() {
        return upward;
    }

    public Station getDownward() {
        return downward;
    }

    public Integer getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upward, section.upward)
                && Objects.equals(downward, section.downward)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upward, downward, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upward=" + upward +
                ", downward=" + downward +
                ", distance=" + distance +
                '}';
    }

    public boolean isUpward(Station station) {
        return this.upward.equals(station);
    }

    public boolean isDownward(Station station) {
        return this.downward.equals(station);
    }
}
