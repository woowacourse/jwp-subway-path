package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.integration.TestFixture.잠실나루역;
import static subway.integration.TestFixture.잠실새내역;
import static subway.integration.TestFixture.잠실역;
import static subway.integration.TestFixture.종합운동장역;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @DisplayName("어떤 구간과 연결되는지 알 수 있다")
    @Test
    void isConnectedToOtherSection() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var otherSection = new Section(잠실역, 잠실새내역, 5);

        assertThat(section.canConnect(otherSection)).isTrue();
    }

    @DisplayName("일부 구간을 포함하는지 알 수 있다")
    @Test
    void coversOtherSection() {
        var parent = new Section(잠실나루역, 잠실새내역, 15);
        var contained = new Section(잠실역, 잠실새내역, 5);

        assertThat(parent.covers(contained)).isTrue();
        assertThat(contained.covers(parent)).isFalse();
    }

    @DisplayName("어떤 역을 포함하는지 알 수 있다")
    @Test
    void containsStation() {
        var section = new Section(잠실나루역, 잠실역, 10);

        assertThat(section.contains(잠실역)).isTrue();
    }

    @DisplayName("일부 구간으로 전체의 나머지 구간을 구한다")
    @Test
    void getPartOtherThanGivenPart() {
        var parent = new Section(잠실나루역, 잠실새내역, 15);
        var part = new Section(잠실나루역, 잠실역, 10);
        var expectedPart = new Section(잠실역, 잠실새내역, 5);

        assertThat(parent.getPartOtherThan(part)).isEqualTo(expectedPart);
    }

    @DisplayName("일부가 아닌 구간으로 나머지 구간을 구하면 예외를 던진다")
    @Test
    void getPartOtherThanNotCoveredPart_throws() {
        var parent = new Section(잠실나루역, 잠실새내역, 15);
        var notPart = new Section(잠실새내역, 종합운동장역, 5);

        assertThatThrownBy(() -> parent.getPartOtherThan(notPart))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이어지는 두 구간을 하나로 병합한다")
    @Test
    void mergeTwoConnectedSections() {
        var upperSection = new Section(잠실나루역, 잠실역, 10);
        var lowerSection = new Section(잠실역, 잠실새내역, 5);

        var mergedSection = upperSection.mergeWith(lowerSection);

        assertThat(mergedSection).isEqualTo(new Section(잠실나루역, 잠실새내역, 15));
    }

    @DisplayName("이어지지 않으면 예외를 던진다")
    @Test
    void mergeDisconnectedSections_throws() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var disconnectedSection = new Section(잠실새내역, 종합운동장역, 5);

        assertThatThrownBy(() -> section.mergeWith(disconnectedSection))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("기점 쪽 역을 알 수 있다")
    @Test
    void getUpperStation() {
        var section = new Section(잠실역, 잠실새내역, 10);

        assertThat(section.getUpperStation()).isEqualTo(잠실역);
    }

    @DisplayName("종점 쪽 역을 알 수 있다")
    @Test
    void getLowerStation() {
        var section = new Section(잠실역, 잠실새내역, 10);

        assertThat(section.getLowerStation()).isEqualTo(잠실새내역);
    }

    @DisplayName("구간의 거리를 알 수 있다")
    @Test
    void getDistance() {
        var section = new Section(잠실역, 잠실새내역, 10);

        assertThat(section.getDistance()).isEqualTo(10);
    }
}
