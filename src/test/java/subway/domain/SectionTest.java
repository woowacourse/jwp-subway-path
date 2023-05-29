package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.TestFixture.잠실나루역;
import static subway.TestFixture.잠실새내역;
import static subway.TestFixture.잠실역;
import static subway.TestFixture.종합운동장역;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.exception.EmptySectionException;
import subway.domain.exception.IllegalSectionException;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @DisplayName("두 역이 모두 있어야 한다")
    @Test
    void requiresBothStation() {
        assertThatThrownBy(() -> new Section(잠실나루역, null, 10))
                .isInstanceOf(EmptySectionException.class);
    }

    @DisplayName("어떤 구간과 연결되는지 알 수 있다")
    @Test
    void hasLinkWithOtherSection() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var otherSection = new Section(잠실역, 잠실새내역, 5);

        assertThat(section.hasLinkWith(otherSection)).isTrue();
    }

    @DisplayName("서로 대칭인 경우는 연결된 것이 아니다.")
    @Test
    void mirroredSectionsAreNotLinked() {
        var section = new Section(잠실나루역, 잠실역, 10);
        var otherSection = new Section(잠실역, 잠실나루역, 10);

        assertThat(section.hasLinkWith(otherSection)).isFalse();
    }

    @DisplayName("일부 구간을 포함하는지 알 수 있다")
    @Test
    void coversOtherSection() {
        var parent = new Section(잠실나루역, 잠실새내역, 15);
        var contained = new Section(잠실역, 잠실새내역, 5);

        assertThat(parent.hasOverlapWith(contained)).isTrue();
        assertThat(contained.hasOverlapWith(parent)).isFalse();
    }

    @DisplayName("어떤 역을 포함하는지 알 수 있다")
    @Test
    void containsStation() {
        var section = new Section(잠실나루역, 잠실역, 10);

        assertThat(section.contains(잠실역)).isTrue();
    }

    @DisplayName("한 구간을 두 부분으로 쪼갠다")
    @Test
    void splitIntoOneAndOtherPart() {
        var parent = new Section(잠실나루역, 잠실새내역, 15);
        var part = new Section(잠실나루역, 잠실역, 10);
        var expectedPart = new Section(잠실역, 잠실새내역, 5);

        assertThat(parent.splitIntoOneAnd(part)).contains(expectedPart);
    }

    @DisplayName("쪼갠 후 상행 역 기준으로 정렬되어 반환된다.")
    @Test
    void splitIntoOneAndOtherPart_orderedByUpperToLower() {
        var parent = new Section(잠실나루역, 잠실새내역, 15);
        var upperPart = new Section(잠실나루역, 잠실역, 10);
        var lowerPart = new Section(잠실역, 잠실새내역, 5);

        assertThat(parent.splitIntoOneAnd(lowerPart))
                .containsExactly(
                        upperPart,
                        lowerPart
                );
    }

    @DisplayName("제공한 부분이 일부가 아니면 예외를 던진다")
    @Test
    void splitIntoOneAndOther_givenNotOverlapped_throws() {
        var section = new Section(잠실나루역, 잠실새내역, 15);
        var notOverlappedSection = new Section(잠실새내역, 종합운동장역, 5);

        assertThatThrownBy(() -> section.splitIntoOneAnd(notOverlappedSection))
                .isInstanceOf(IllegalSectionException.class);
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
                .isInstanceOf(IllegalSectionException.class);
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

        assertThat(section.getDistance()).isEqualTo(new Distance(10));
    }
}
