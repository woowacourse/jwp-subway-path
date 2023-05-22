package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidSectionException;

class LineTest {

    private Line line;
    private Station upward;
    private Station downward;

    @BeforeEach
    void setUp() {
        upward = new Station(1L, "잠실역");
        downward = new Station(2L, "종합운동장역");
        final List<Section> sections = List.of(
                new Section(upward, downward, 10),
                new Section(downward, Station.TERMINAL, 0)
        );
        line = new Line(1L, "2호선", "초록색", 500, new ArrayList<>(sections));
    }

    @Nested
    @DisplayName("노선 역 추가 시 ")
    class AddSection {

        @Test
        @DisplayName("노선에 역을 최초 추가한다.")
        void addFirstSection() {
            final Station upward = new Station(1L, "잠실역");
            final Station downward = new Station(2L, "종합운동장역");
            final Line line = new Line(1L, "2호선", "초록색", 500);

            line.addSection(upward, downward, 10);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(10)
            );
        }

        @Test
        @DisplayName("중간에 상행역을 추가한다.")
        void addUpwardSection() {
            final Station additionStation = new Station(3L, "잠실새내역");

            line.addSection(additionStation, downward, 5);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, additionStation, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(5, 5)
            );
        }

        @Test
        @DisplayName("중간에 하행역을 추가한다.")
        void addDownwardSection() {
            final Station additionStation = new Station(3L, "잠실새내역");

            line.addSection(upward, additionStation, 5);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, additionStation, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(5, 5)
            );
        }

        @Test
        @DisplayName("맨 앞에 역을 추가한다.")
        void addSectionAtFirst() {
            final Station additionStation = new Station(3L, "잠실새내역");

            line.addSection(additionStation, upward, 5);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(additionStation, upward, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(5, 10)
            );
        }

        @Test
        @DisplayName("맨 뒤에 역을 추가한다.")
        void addSectionAtLast() {
            final Station additionStation = new Station(3L, "잠실새내역");

            line.addSection(downward, additionStation, 5);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, downward, additionStation),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(10, 5)
            );
        }

        @Test
        @DisplayName("역이 둘다 존재한다면 예외를 던진다.")
        void addSectionWithExistStations() {
            assertThatThrownBy(() -> line.addSection(upward, downward, 5))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("두 역이 이미 노선에 존재합니다.");
        }

        @Test
        @DisplayName("역이 둘다 존재하지 않으면 예외를 던진다.")
        void addSectionWithoutExistStations() {
            final Station newUpward = new Station(3L, "잠실새내역");
            final Station newDownward = new Station(4L, "사당역");

            assertThatThrownBy(() -> line.addSection(newUpward, newDownward, 5))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("연결할 역 정보가 없습니다.");
        }

        @Test
        @DisplayName("추가될 역의 거리가 추가될 위치의 두 역 사이보다 크거나 같으면 예외를 던진다.")
        void addSectionWithInvalidRangeDistance() {
            final Station additionStation = new Station(3L, "잠실새내역");

            assertThatThrownBy(() -> line.addSection(upward, additionStation, 10))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessage("추가될 역의 거리는 추가될 위치의 두 역사이의 거리보다 작아야합니다.");
        }
    }

    @Nested
    @DisplayName("노선에서 역 제거할 시 ")
    class DeleteStation {

        @Test
        @DisplayName("역이 2개일 때 역을 제거한다.")
        void deleteStationAtInitialState() {
            line.deleteStation(upward);

            final List<Station> result = line.getStations();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("역이 2개가 아닐 때 맨 앞의 역을 제거한다.")
        void deleteStationAtFirst() {
            final Station additionStation = new Station(3L, "잠실새내역");
            line.addSection(upward, additionStation, 3);

            line.deleteStation(upward);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(additionStation, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(7)
            );
        }

        @Test
        @DisplayName("역이 2개가 아닐 때 중간의 역을 제거한다.")
        void deleteStationBetweenStations() {
            final Station additionStation = new Station(3L, "잠실새내역");
            line.addSection(upward, additionStation, 3);

            line.deleteStation(additionStation);

            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(10)
            );
        }

        @Test
        @DisplayName("역이 존재하지 않을 때 예외를 던진다.")
        void deleteStationWithNotExistStation() {
            assertThatThrownBy(() -> line.deleteStation(new Station(3L, "잠실새내역")))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("노선에 해당 역이 존재하지 않습니다.");
        }
    }
}
