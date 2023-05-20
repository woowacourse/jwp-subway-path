package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixture.DistanceFixture.DISTANCE_5;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.StationFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {
    @Test
    @DisplayName("구간(section)을 생성한다.")
    void createSection() {
        assertDoesNotThrow(
                () -> Section.of(STATION_강남, STATION_잠실, DISTANCE_5));
    }

    @Test
    @DisplayName("해당 구간과 비교하고자 하는 구간의 상행역이 같으면 true, 아니면 false를 반환한다.")
    void equalsUpStation() {
        assertAll(
                () -> assertThat(SECTION_강남_암사_5
                        .equalsUpStation(SECTION_강남_잠실_5))
                        .isTrue(),

                () -> assertThat(SECTION_강남_암사_5
                        .equalsUpStation(SECTION_잠실_강남_5))
                        .isFalse()
        );
    }

    @Test
    @DisplayName("해당 구간과 비교하고자 하는 하행역이 같으면 true, 아니면 false를 반환한다.")
    void equalsDownStation() {
        assertAll(
                () -> assertThat(SECTION_강남_암사_5
                        .equalsDownStation(SECTION_몽촌토성_암사_5))
                        .isTrue(),
                () -> assertThat(SECTION_강남_암사_5
                        .equalsDownStation(SECTION_잠실_강남_5))
                        .isFalse()
        );
    }

    @Test
    @DisplayName("해당 구간의 다음 구간이면 true, 아니면 false를 반환한다")
    void isNextSection() {
        assertAll(
                () -> assertThat(SECTION_강남_잠실_5
                        .isNextSection(SECTION_잠실_강남_5))
                        .isTrue(),
                () -> assertThat(SECTION_강남_잠실_5
                        .isNextSection(SECTION_몽촌토성_암사_5))
                        .isFalse()
        );
    }

    @Test
    @DisplayName("해당 구간에 역이 포함되면 true, 아니면 false를 반환한다.")
    void contains() {
        assertAll(
                () -> assertThat(SECTION_강남_잠실_5
                        .contains(STATION_강남))
                        .isTrue(),
                () -> assertThat(SECTION_강남_잠실_5
                        .contains(STATION_몽촌토성))
                        .isFalse()
        );
    }
}
