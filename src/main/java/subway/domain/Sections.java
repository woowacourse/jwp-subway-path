package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static subway.domain.Direction.DOWN;
import static subway.domain.Direction.UP;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Sections addSection(final Section newSection) {
        final List<Section> nowSections = getSections();
        final Station newUpStation = newSection.getUpStation();
        final Station newDownStation = newSection.getDownStation();
        final List<Station> nowUpStations = getUpStations(nowSections);
        final List<Station> nowDownStations = getDownStations(nowSections);
        //이미 경로가 존재하는 경우
        if (isContainsStation(newUpStation, nowUpStations) && isContainsStation(newDownStation, nowDownStations)) {
            throw new IllegalArgumentException("이미 존재하는 경로를 추가할 수 없습니다");
        }

        // 상행역 기준 사이 경로 추가하는 경우
        if ((isContainsStation(newUpStation, nowUpStations)) && (!isContainsStation(newDownStation, nowDownStations))) {
            return addBetweenBasedUp(newSection, nowSections, newUpStation, nowUpStations);
        }
        //하행역 기준 사이 경로 추가하는 경우
        if ((!isContainsStation(newUpStation, nowUpStations)) && isContainsStation(newDownStation, nowDownStations)) {
            return addBetWeenBasedDown(newSection, nowSections, newDownStation, nowDownStations);
        }
        //경로가 이어지지 않은 경우 검증
        validateSectionsIsConnected(newUpStation, newDownStation, nowUpStations, nowDownStations);
        //새로운 경로 추가
        nowSections.add(newSection);
        return new Sections(nowSections);
    }

    private Sections addBetWeenBasedDown(final Section newSection, final List<Section> nowSections, final Station newDownStation, final List<Station> nowDownStations) {
        final Station targetDownStation = extractTargetStation(newDownStation, nowDownStations);
        final Section targetSection = extractTargetSection(nowSections, targetDownStation, Direction.DOWN);
        validateSectionDirection(newSection, targetSection);
        final Section newUpSection = new Section(targetSection.getUpStation(), newSection.getUpStation(), targetSection.getDistance() - newSection.getDistance());
        nowSections.add(newSection);
        nowSections.add(newUpSection);
        nowSections.remove(targetSection);
        return new Sections(nowSections);
    }

    private Sections addBetweenBasedUp(final Section newSection, final List<Section> nowSections, final Station newUpStation, final List<Station> nowUpStations) {
        // 상행역 중 추가하려는 경로 상행역이 있는지 확인 후 추출
        final Station targetUpStation = extractTargetStation(newUpStation, nowUpStations);

        //경로 중 추가하려는 경로의 상행역이 상행역으로 있는 경로가 존재하는지 확인 후 추출
        final Section targetSection = extractTargetSection(nowSections, targetUpStation, UP);

        validateSectionDirection(newSection, targetSection);

        //기존 경로를 삭제 후 새로운 경로 둘을 추가
        final Section newDownSection = new Section(newSection.getDownStation(), targetSection.getDownStation(), targetSection.getDistance() - newSection.getDistance());
        nowSections.add(newSection);
        nowSections.add(newDownSection);
        nowSections.remove(targetSection);
        return new Sections(nowSections);
    }


    public Sections deleteSection(final Station targetStation) {
        final List<Section> nowSections = getSections();
        final List<Station> nowUpStations = getUpStations(nowSections);
        final List<Station> nowDownStations = getDownStations(nowSections);
        //삭제하려는 역이 존재하지 않으면 예외
        validateStationIsExists(targetStation, nowUpStations, nowDownStations);
        if (getSections().size() == 1) {
            return new Sections(new ArrayList<>());
        }
        //종점 역이 제거되는 경우
        //상행역/하행역 차집합 통해 상행/하행 종점 구함
        Station lastUpstation = getLastUpstation(nowSections, nowUpStations);
        Station lastDownStation = getLastDownStation(nowSections, nowDownStations);
        //지우려는 역이 종점 상행역이면
        if (lastUpstation.equals(targetStation)) {
            return deleteEndSection(nowSections, targetStation, UP);
        }
        //종점 하행역이 제거되는 경우
        if (lastDownStation.equals(targetStation)) {
            return deleteEndSection(nowSections, targetStation, DOWN);
        }
        //사이 역이 제거되는 경우
        deleteBetweenSection(targetStation, nowSections);
        return new Sections(nowSections);

    }

    private void deleteBetweenSection(final Station targetStation, final List<Section> nowSections) {
        final Section targetUpSection = extractTargetSection(nowSections, targetStation, DOWN);
        final Section targetDownSection = extractTargetSection(nowSections, targetStation, UP);
        final Long newDistance = targetUpSection.getDistance() + targetDownSection.getDistance();
        final Section newSection = new Section(targetUpSection.getUpStation(), targetDownSection.getDownStation(), newDistance);
        nowSections.remove(targetUpSection);
        nowSections.remove(targetDownSection);
        nowSections.add(newSection);
    }

    private Station getLastDownStation(final List<Section> nowSections, final List<Station> nowDownStations) {
        nowDownStations.removeAll(getUpStations(nowSections));
        Station lastDownStation = nowDownStations.get(0);
        return lastDownStation;
    }

    private Station getLastUpstation(final List<Section> nowSections, final List<Station> nowUpStations) {
        nowUpStations.removeAll(getDownStations(nowSections));
        Station lastUpstation = nowUpStations.get(0);
        return lastUpstation;
    }

    private Sections deleteEndSection(final List<Section> nowSections, final Station targetStation, final Direction direction) {
        Section targetSection = extractTargetSection(nowSections, targetStation, direction);
        //경로 하나 제거
        nowSections.remove(targetSection);
        return new Sections(nowSections);
    }

    private void validateStationIsExists(final Station targetStation, final List<Station> nowUpStations, final List<Station> nowDownStations) {
        if ((!isContainsStation(targetStation, nowUpStations)) && (!isContainsStation(targetStation, nowDownStations))) {
            throw new IllegalArgumentException("이미 존재하는 역만 삭제할 수 있습니다");
        }
    }

    public List<Station> sortStations() {
        final List<Section> nowSections = getSections();
        final List<Station> nowUpStations = getUpStations(nowSections);

        Station lastUpstation = getLastUpstation(nowSections, nowUpStations);
        List<Station> stations = new ArrayList<>(List.of(lastUpstation));

        Station targetStation = lastUpstation;
        defineRoute(stations, targetStation);
        return stations;
    }

    private void defineRoute(final List<Station> stations, Station targetStation) {
        while (stations.size() < sections.size() + 1) {
            targetStation = seekNextStation(stations, targetStation);
        }
    }

    private Station seekNextStation(final List<Station> stations, Station targetStation) {
        for (Section section : sections) {
            if (section.getUpStation().equals(targetStation)) {
                Station nextStation = section.getDownStation();
                stations.add(nextStation);
                targetStation = nextStation;
                break;
            }
        }
        return targetStation;
    }


    private Section extractTargetSection(final List<Section> nowSections, final Station targetStation, final Direction direction) {
        return nowSections.stream()
                .filter(section -> section.isHavingSameStationWithDirection(targetStation, direction))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));
    }

    private Station extractTargetStation(final Station newStation, final List<Station> nowStations) {
        return nowStations.stream()
                .filter(station -> station.equals(newStation))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다"));
    }

    private boolean isContainsStation(final Station newStation, final List<Station> nowStations) {
        return nowStations.stream()
                .anyMatch(station -> station.equals(newStation));
    }

    private List<Station> getDownStations(final List<Section> nowSections) {
        return nowSections.stream()
                .map(innerSection -> innerSection.getDownStation())
                .collect(Collectors.toList());
    }

    private List<Station> getUpStations(final List<Section> nowSections) {
        return nowSections.stream()
                .map(innerSection -> innerSection.getUpStation())
                .collect(Collectors.toList());
    }

    private void validateSectionDirection(final Section newSection, final Section targetSection) {
        if (newSection.getDistance() >= targetSection.getDistance()) {
            throw new IllegalArgumentException("새로운 경로 거리는 기존 경로보다 짧아야 합니다");
        }
    }

    private void validateSectionsIsConnected(final Station newUpStation, final Station newDownStation, final List<Station> nowUpStations, final List<Station> nowDownStations) {
        if (sections.size() > 0) {
            if ((!isContainsStation(newUpStation, nowDownStations)) && (!isContainsStation(newDownStation, nowUpStations))) {
                throw new IllegalArgumentException("이어지지 않는 경로를 추가할 수 없습니다");
            }
        }
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
