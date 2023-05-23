package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

class SectionTest {

    @DisplayName("id가 같으면 equals에서 true를 반환한다.")
    @Test
    void equalsTest() {
        final Section a = Section.builder()
                .id(1L)
                .lineId(2L)
                .upStation(new Station("선릉역"))
                .downStation(new Station("잠실역"))
                .distance(new Distance(3))
                .build();

        final Section b = Section.builder()
                .id(1L)
                .lineId(3L)
                .upStation(new Station("낙성대역"))
                .downStation(new Station("사당역"))
                .distance(new Distance(4))
                .build();

        assertThat(a).isEqualTo(b);
    }

    @DisplayName("id가 다르면 equals에서 false를 반환한다.")
    @Test
    void notEqualsTest() {
        final Section a = Section.builder()
                .id(1L)
                .lineId(2L)
                .upStation(new Station("선릉역"))
                .downStation(new Station("잠실역"))
                .distance(new Distance(3))
                .build();

        final Section b = Section.builder()
                .id(2L)
                .lineId(2L)
                .upStation(new Station("선릉역"))
                .downStation(new Station("잠실역"))
                .distance(new Distance(3))
                .build();

        assertThat(a).isNotEqualTo(b);
    }

    @DisplayName("id 없이 builder 생성")
    @Test
    void builderNoIdTest() {
        final Section section = Section.builder()
                .lineId(1L)
                .upStation(2L)
                .downStation(3L)
                .distance(4)
                .build();

        Assertions.assertAll(
                () -> assertThat(section.getId()).isNull(),
                () -> assertThat(section.getLineId()).isEqualTo(1L),
                () -> assertThat(section.getUpStation().getId()).isEqualTo(2L),
                () -> assertThat(section.getUpStation().getName()).isNull(),
                () -> assertThat(section.getDownStation().getId()).isEqualTo(3L),
                () -> assertThat(section.getDownStation().getName()).isNull(),
                () -> assertThat(section.getDistance().getValue()).isEqualTo(4)
        );
    }

    @DisplayName("포장 없이 builder 생성")
    @Test
    void builderNoWrapperTest() {
        final Section section = Section.builder()
                .id(1L)
                .lineId(1L)
                .upStation(2L)
                .downStation(3L)
                .distance(4)
                .build();

        Assertions.assertAll(
                () -> assertThat(section.getId()).isEqualTo(1L),
                () -> assertThat(section.getLineId()).isEqualTo(1L),
                () -> assertThat(section.getUpStation().getId()).isEqualTo(2L),
                () -> assertThat(section.getUpStation().getName()).isNull(),
                () -> assertThat(section.getDownStation().getId()).isEqualTo(3L),
                () -> assertThat(section.getDownStation().getName()).isNull(),
                () -> assertThat(section.getDistance().getValue()).isEqualTo(4)
        );
    }

    @DisplayName("builder 생성")
    @Test
    void builderTest() {
        final Section section = Section.builder()
                .id(1L)
                .lineId(1L)
                .upStation(new Station(2L, "잠실역"))
                .downStation(new Station(3L, "선릉역"))
                .distance(new Distance(4))
                .build();

        Assertions.assertAll(
                () -> assertThat(section.getId()).isEqualTo(1L),
                () -> assertThat(section.getLineId()).isEqualTo(1L),
                () -> assertThat(section.getUpStation().getId()).isEqualTo(2L),
                () -> assertThat(section.getUpStation().getName()).isEqualTo("잠실역"),
                () -> assertThat(section.getDownStation().getId()).isEqualTo(3L),
                () -> assertThat(section.getDownStation().getName()).isEqualTo("선릉역"),
                () -> assertThat(section.getDistance()).isEqualTo(new Distance(4))
        );
    }
}
