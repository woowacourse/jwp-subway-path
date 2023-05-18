package subway.domain.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    private static final Station A = new Station(1L, "A");
    private static final Station B = new Station(2L, "B");
    private static final Station C = new Station(3L, "C");
    private static final Station D = new Station(4L, "D");

    private Sections sections;

    @BeforeEach
    void setUp() {
        // given
        final List<Section> sectionList = List.of(
                new Section(3L, C, D, new Distance(5)),
                new Section(1L, A, B, new Distance(10)),
                new Section(2L, B, C, new Distance(3))
        );
        sections = new Sections(sectionList);
    }

    @DisplayName("Sections로부터 정렬된 Station 리스트를 가져올 수 있다.")
    @Test
    void getOrderedStations() {
        // when
        final List<Station> stations = sections.getOrderedStations();

        // then
        assertThat(stations).containsExactly(A, B, C, D);
    }

    @DisplayName("방향에 따라 새로운 Station을 기존 Sections에 추가할 수 있다.")
    @Nested
    class AddNewStationTest {

        final Station existStation = D;
        final Station newStation = new Station(5L, "E");
        final Distance distance = new Distance(3);

        @DisplayName("상행 방향에 추가한다.")
        @Test
        void add_upper() {
            // when
            final DirectionStrategy directionStrategy = new UpDirectionStrategy();
            final Sections updated = sections.add(existStation, newStation, directionStrategy, distance);

            // then
            final List<Station> stations = updated.getOrderedStations();
            assertThat(stations).containsExactly(A, B, C, newStation, D);
        }

        @DisplayName("하행 방향에 추가한다.")
        @Test
        void add_down() {
            // when
            final DirectionStrategy directionStrategy = new DownDirectionStrategy();
            final Sections updated = sections.add(existStation, newStation, directionStrategy, distance);

            // then
            final List<Station> stations = updated.getOrderedStations();
            assertThat(stations).containsExactly(A, B, C, D, newStation);
        }
    }

    @DisplayName("기존 역을 삭제한다.")
    @Nested
    class DeleteStationTest {

        @DisplayName("B를 삭제한다.")
        @Test
        void delete1() {
            // when
            final Sections updated = sections.delete(B);

            // then
            final List<Station> stations = updated.getOrderedStations();
            assertThat(stations).containsExactly(A, C, D);
        }

        @DisplayName("A를 삭제한다.")
        @Test
        void delete2() {
            // when
            final Sections updated = sections.delete(A);

            // then
            final List<Station> stations = updated.getOrderedStations();
            assertThat(stations).containsExactly(B, C, D);
        }

        @DisplayName("D를 삭제한다.")
        @Test
        void delete3() {
            // when
            final Sections updated = sections.delete(D);

            // then
            final List<Station> stations = updated.getOrderedStations();
            assertThat(stations).containsExactly(A, B, C);
        }
    }
}
