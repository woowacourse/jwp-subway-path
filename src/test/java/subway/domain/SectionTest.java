package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionTest {

    @Test
    @DisplayName("역의 이름은 중복될 수 없습니다")
    void SectionNameDuplicate() {
        //given
        Assertions.assertThatThrownBy(
                () -> new Section(1L, new Station("잠실"), new Station("잠실"), 10L)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("기존의 역과 중복되는지 판별.")
    void validateDuplicateSection() {
        final Section section1 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);
        final Section section2 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);

        assertThat(section1.validateDuplicateSection(section2)).isTrue();
    }

    @Test
    @DisplayName("중간의 역을 제거 시 양옆의 역이 이어져야한다.")
    void mergedSection() {
        final Section section1 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);
        final Section section2 = new Section(1L, new Station("라빈"), new Station("허브"), 10L);

        Section section = section1.mergedSection(section2, new Station("라빈"));

        assertAll(
                () -> assertThat(section.getUpStation().getName()).isEqualTo("비버"),
                () -> assertThat(section.getDownStation().getName()).isEqualTo("허브"),
                () -> assertThat(section.getDistance()).isEqualTo(20L)
        );
    }

    @Test
    @DisplayName("추가하는 곳이 종점인지 판별")
    void validateEqualEndPoint() {
        final Section section1 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);

        assertAll(
                () -> assertThat(section1.validateEqualEndPoint(new Station("허브"), new Station("비버"))).isTrue(),
                () -> assertThat(section1.validateEqualEndPoint(new Station("비버"), new Station("허브"))).isFalse()
        );
    }

    @Test
    @DisplayName("새로운 역을 등록할 경우 기존 역 사이 길이보다 이상인지 판별")
    void validateDistance() {
        final Section section1 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);
        final Section section2 = new Section(1L, new Station("비버"), new Station("허브"), 100L);

        assertThat(section1.validateDistance(section2)).isTrue();
    }

    @Test
    @DisplayName("구간에 해당역이 있는지 판변")
    void hasStation() {
        final Section section1 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);

        assertThat(section1.hasStation(new Station("라빈"))).isTrue();
    }
}