package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.TestFixture.잠실나루역;
import static subway.TestFixture.잠실새내역;
import static subway.TestFixture.잠실역;
import static subway.TestFixture.종합운동장역;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.exception.DuplicateSectionException;
import subway.domain.exception.IllegalSectionException;
import subway.domain.exception.NoSuchStationException;

@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        this.line = new Line("2호선", "green");
    }

    @DisplayName("이미 등록된 구간을 추가하면 예외를 던진다")
    @Test
    void addExistingSection_throws() {
        var section = new Section(잠실나루역, 잠실역, 10);
        line.add(section);

        assertThatThrownBy(() -> line.add(section))
                .isInstanceOf(DuplicateSectionException.class);
    }

    @DisplayName("구간이 있을 때, 비연결 구간을 추가하면 예외를 던진다")
    @Test
    void addDisconnectedSectionWhenAnySectionExistsThrows() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var disconnectedSection = new Section(잠실새내역, 종합운동장역, 5);
        line.add(section);

        assertThatThrownBy(() -> line.add(disconnectedSection))
                .isInstanceOf(IllegalSectionException.class);
    }

    @DisplayName("구간이 없을 땐 비연결 구간을 그대로 추가한다")
    @Test
    void addDisconnectedSectionWhenNoSectionExists() {
        var section = new Section(잠실역, 잠실새내역, 10);

        line.add(section);

        assertThat(line.getStations()).contains(잠실역, 잠실새내역);
    }

    @DisplayName("종점 뒤에 구간을 추가한다")
    @Test
    void addToLowerOfArrival() {
        var upperSection = new Section(잠실나루역, 잠실역, 10);
        var lowerSection = new Section(잠실역, 잠실새내역, 5);
        line.add(upperSection);

        line.add(lowerSection);

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실역, 잠실새내역);
    }

    @DisplayName("기점 앞에 구간을 추가한다")
    @Test
    void addToUpperOfDeparture() {
        var lowerSection = new Section(잠실역, 잠실새내역, 5);
        var upperSection = new Section(잠실나루역, 잠실역, 10);
        line.add(lowerSection);

        line.add(upperSection);

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실역, 잠실새내역);
    }

    @DisplayName("역 사이에 추가할 수 있다")
    @Test
    void addBetweenStations() {
        var existingSection = new Section(잠실나루역, 잠실새내역, 15);
        var newSection = new Section(잠실역, 잠실새내역, 5);
        line.add(existingSection);

        line.add(newSection);

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실역, 잠실새내역);
    }

    @DisplayName("기점 역을 제거한다")
    @Test
    void removeDepartureStation() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var other = new Section(잠실역, 잠실새내역, 5);
        line.add(section);
        line.add(other);

        line.remove(잠실나루역);

        assertThat(line.getStations())
                .containsExactly(잠실역, 잠실새내역);
    }

    @DisplayName("종점 역을 제거한다")
    @Test
    void removeArrivalStation() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var other = new Section(잠실역, 잠실새내역, 5);
        line.add(section);
        line.add(other);

        line.remove(잠실새내역);

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실역);
    }

    @DisplayName("중간 역을 제거한다")
    @Test
    void removeStation() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var other = new Section(잠실역, 잠실새내역, 5);
        var another = new Section(잠실새내역, 종합운동장역, 5);
        line.add(section);
        line.add(other);
        line.add(another);

        line.remove(잠실역);

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실새내역, 종합운동장역);
    }

    @DisplayName("없는 역은 제거할 수 없다")
    @Test
    void deleteNotContainedStation_throws() {
        var section = new Section(잠실새내역, 종합운동장역, 5);
        line.add(section);

        assertThatThrownBy(() -> line.remove(잠실역))
                .isInstanceOf(NoSuchStationException.class);
    }
}