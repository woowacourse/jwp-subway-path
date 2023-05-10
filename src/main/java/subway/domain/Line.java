package subway.domain;

import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;

 //   private List<Section> sections;

    private Station headStation;


    public Line() {
    }

    public Line(String name, String color, Station headStation){
        validate(headStation);
        //headStation이 emptyStation이면 안 된다

        this.name=name;
        this.color=color;
        this.headStation=headStation;
    }

    private void validate(Station headStation) {
        validateNotEmptyStation(headStation);
        validateHaveTwoStations(headStation);
    }

    private void validateNotEmptyStation(Station headStation) {
        if(headStation.equals(Station.emptyStation)){
            throw new IllegalArgumentException("상행종점은 비어있을 수 없습니다.");
        }
    }

    private void validateHaveTwoStations(Station headStation) {
        if(headStation.isDownEndStation()){
            throw new IllegalArgumentException("노선을 생성할 때 최소 2개 이상의 역이 존재해야 합니다.");
        }
    }


    //상행역, 하행역 거리 (상행역과 하행역중 하나는 내가 삽입하고 싶은 역이 된다)
    //상행역과 하행역은 빈 역이 될 수 없다

//    public void addSection(Section section){
//        //section은 추가될 역
//        //section의 upStation, downStation 중에 하나가 sections안에 있는지 확인해야해
//        //나머지 하나는 존재하지 않아야함
//        //존재하는 역의 index를 뽑음
//        //sections에 그 index값을 이용해서 추가
//
//        //sections.add(index, section);
//    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
