package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.integration.TestFixture.잠실나루역;
import static subway.integration.TestFixture.잠실새내역;
import static subway.integration.TestFixture.잠실역;
import static subway.integration.TestFixture.종합운동장역;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Line2Test {

    @DisplayName("구간이 있을 때, 비연결 구간을 추가하면 예외를 던진다")
    @Test
    void addDisconnectedSectionWhenAnySectionExistsThrows() {
        var line = new Line2("2호선");
        var section = new Section(잠실나루역, 잠실역, 10);
        var disconnectedSection = new Section(잠실새내역, 종합운동장역, 5);
        line.add(section);

        assertThatThrownBy(() -> line.add(disconnectedSection))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("구간이 없을 땐 비연결 구간을 그대로 추가한다")
    @Test
    void addDisconnectedSectionWhenNoSectionExists() {
        var line = new Line2("2호선");
        var section = new Section(잠실역, 잠실새내역, 10);

        line.add(section);

        assertThat(line.getStations()).contains(잠실역, 잠실새내역);
    }

    @DisplayName("종점 뒤에 구간을 추가한다")
    @Test
    void addToLowerOfArrival() {
        var line = new Line2("2호선");
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
        var line = new Line2("2호선");
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
        var line = new Line2("2호선");
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
        var line = new Line2("2호선");
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
        var line = new Line2("2호선");
        var section = new Section(잠실나루역, 잠실역, 10);
        var other = new Section(잠실역, 잠실새내역, 5);
        line.add(section);
        line.add(other);

        line.remove(잠실새내역);
        System.out.println("line.getSections() = " + line.getSections());

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실역);
    }

    @DisplayName("중간 역을 제거한다")
    @Test
    void removeStation() {
        var line = new Line2("2호선");
        var section = new Section(잠실나루역, 잠실역, 10);
        var other = new Section(잠실역, 잠실새내역, 5);
        var another = new Section(잠실새내역, 종합운동장역, 5);
        line.add(section);
        line.add(other);
        line.add(another);

        line.remove(잠실역);
        System.out.println("line.getSections() = " + line.getSections());

        assertThat(line.getStations())
                .containsExactly(잠실나루역, 잠실새내역, 종합운동장역);
    }
}
