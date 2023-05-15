package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class SectionTest {

    Station station = Station.of(1L, "잠실역");
    Section section;

    @BeforeEach
    void setUp() {
        section = Section.create();
    }

    @Test
    void add_메소드는_호출하면_인접한_역과의_구역_정보를_저장한다() {
        final Distance distance = Distance.from(10);

        section.add(station, distance, Direction.UP);

        assertThat(section.findAllStation()).hasSize(1);
    }

    @Test
    void delete_메소드는_인접한_역을_전달하면_인접한_역과의_구역_정보를_삭제한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        section.delete(station);

        assertThat(section.findAllStation()).isEmpty();
    }

    @Test
    void delete_메소드는_인접하지_않은_역을_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> section.delete(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인접하지 않은 역 입니다.");
    }

    @Test
    void calculateMiddleDistance_메소드는_연결된_역과_거리를_전달하면_거리의_차를_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final Distance actual = section.calculateMiddleDistance(station, Distance.from(5));

        assertThat(actual.getDistance()).isEqualTo(5);
    }

    @Test
    void calculateMiddleDistance_메소드는_연결되지_않은_역을_전달하면_예외가_발생한다() {
        final Distance distance = Distance.from(5);

        assertThatThrownBy(() -> section.calculateMiddleDistance(station, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인접하지 않은 역 입니다.");
    }

    @Test
    void findDirectionByStation_메소드는_인접한_역을_전달하면_이어진_방향을_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final Direction actual = section.findDirectionByStation(station);

        assertThat(actual).isSameAs(Direction.UP);
    }

    @Test
    void findDirectionByStation_인접하지_않은_역을_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> section.findDirectionByStation(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인접하지 않은 역 입니다.");
    }

    @Test
    void findDistanceByStation_메소드는_인접한_역을_전달하면_인접한_역의_거리를_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final Distance actual = section.findDistanceByStation(station);

        assertThat(actual.getDistance()).isEqualTo(distance.getDistance());
    }

    @Test
    void findDistanceByStation_인접하지_않은_역을_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> section.findDistanceByStation(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인접하지 않은 역 입니다.");
    }

    @Test
    void findEndStationPathDirection_메소드는_종점_역일_경우_호출하면_방향을_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final Direction actual = section.findEndStationPathDirection();

        assertThat(actual).isSameAs(Direction.UP);
    }

    @Test
    void findEndStationPathDirection_메소드는_종점_역이_아닌_경우_호출하면_예외가_발생한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);
        section.add(Station.of(2L, "잠실나루역"), distance, Direction.DOWN);

        assertThatThrownBy(() -> section.findEndStationPathDirection())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역은 종점역이 아닙니다.");
    }

    @Test
    void findStationByDirection_메소드는_방향을_전달하면_해당_방향으로_인접한_역이_있는_경우_역을_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final Optional<Station> actual = section.findStationByDirection(Direction.UP);

        assertThat(actual).isPresent();
    }

    @Test
    void findStationByDirection_메소드는_방향을_전달하면_해당_방향으로_인접한_역이_없는_경우_빈_Optional을_반환한다() {
        final Optional<Station> actual = section.findStationByDirection(Direction.UP);

        assertThat(actual).isEmpty();
    }

    @Test
    void isTerminalStation_메소드는_종점_역인_경우_true를_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final boolean actual = section.isTerminalStation();

        assertThat(actual).isTrue();
    }

    @Test
    void isTerminalStation_메소드는_종점_역이_아닌_경우_false를_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);
        section.add(Station.of(2L, "잠실나루역"), distance, Direction.DOWN);

        final boolean actual = section.isTerminalStation();

        assertThat(actual).isFalse();
    }

    @Test
    void isConnect_메소드는_인접한_역을_전달하면_true를_반환한다() {
        final Distance distance = Distance.from(10);
        section.add(station, distance, Direction.UP);

        final boolean actual = section.isConnect(station);

        assertThat(actual).isTrue();
    }

    @Test
    void isConnect_메소드는_인접하지_않은_역을_전달하면_false를_반환한다() {
        final boolean actual = section.isConnect(station);

        assertThat(actual).isFalse();
    }
}
