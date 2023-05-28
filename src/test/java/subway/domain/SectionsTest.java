package subway.domain;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SectionsTest {

    @DisplayName("Sections가 정상적으로 생성된다.")
    @Test
    void Sections() {
        // given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 잠실역 = new Station(2L, "선릉역");

        Section 강남역_선릉역 = new Section(강남역, 선릉역, 5);
        Section 선릉역_잠실역 = new Section(선릉역, 잠실역, 10);

        // when & then
        assertDoesNotThrow(() -> new Sections(new ArrayList<>(List.of(강남역_선릉역, 선릉역_잠실역))));
    }

    @DisplayName("Sections에 Section이 정상적으로 추가된다.")
    @Nested
    class addTest {

        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 잠실역 = new Station(3L, "잠실역");
        Station 사당역 = new Station(4L, "사당역");
        Station 역삼역 = new Station(5L, "역삼역");

        Section 사당역_강남역 = new Section(사당역, 강남역, 10);
        Section 강남역_선릉역 = new Section(강남역, 선릉역, 5);
        Section 선릉역_잠실역 = new Section(선릉역, 잠실역, 10);

        Sections sections;

        @BeforeEach
        void setUp() {
            sections = new Sections(new ArrayList<>(Collections.emptyList()));
            sections.add(강남역_선릉역);
        }

        @DisplayName("구간이 없는 경우")
        @Test
        void add_AnySectionInLine() {
            // when
            sections.clear();
            sections.add(강남역_선릉역);

            // then
            assertThat(sections.getSections())
                    .contains(강남역_선릉역);
        }

        @DisplayName("하행 종점에 추가하는 경우: 강남-선릉의 하행 종점에 잠실 추가")
        @Test
        void add_DownFinalDirection() {
            // when
            sections.add(선릉역_잠실역);

            // then
            assertThat(sections.getSections())
                    .contains(강남역_선릉역, 선릉역_잠실역);
        }

        @DisplayName("상행 종점에 추가하는 경우: 강남-선릉의 상행 종점에 사당 추가")
        @Test
        void add_UpFinalDirection() {
            // when
            sections.add(사당역_강남역);

            // then
            assertThat(sections.getSections())
                    .contains(사당역_강남역, 강남역_선릉역);
        }

        @DisplayName("기준이 되는 역의 하행에 추가하는 경우: 강남-선릉 사이에 역삼 추가")
        @Test
        void add_DownDirection() {
            // given
            Section 강남역_역삼역 = new Section(강남역, 역삼역, 3);

            // when
            sections.add(강남역_역삼역);

            // then
            Section 정렬된_역삼역_선릉역 = new Section(역삼역, 선릉역, 2, 2);
            assertThat(sections.getSections())
                    .contains(강남역_역삼역, 정렬된_역삼역_선릉역);
        }

        @DisplayName("기준이 되는 역의 상행에 추가하는 경우: 강남-선릉 사이에 역삼 추가")
        @Test
        void add_UpDirection() {
            // given
            Section 역삼역_선릉역 = new Section(역삼역, 선릉역, 2);

            // when
            sections.add(역삼역_선릉역);

            // then
            Section 정렬된_강남역_역삼역 = new Section(강남역, 역삼역, 3, 1);
            assertThat(sections.getSections())
                    .contains(정렬된_강남역_역삼역, 역삼역_선릉역);
        }

        @DisplayName("구간에 기준이 되는 역이 존재하지 않는 경우 IllegalArgumentException 이 발생한다.")
        @Test
        void add_AnySection() {
            // given
            Station 교대역 = new Station(5L, "교대역");
            Station 종합운동장역 = new Station(6L, "종합운동장역");
            Section 교대역_종합운동장역 = new Section(교대역, 종합운동장역, 2);

            // when & then
            assertThatThrownBy(() -> sections.add(교대역_종합운동장역))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선에 기준이 되는 역이 존재하지 않습니다.");
        }

        @DisplayName("유효하지 않은 거리의 구간을 추가하는 경우 IllegalArgumentException 이 발생한다.")
        @Test
        void add_InvalidDistance() {
            // given
            Section 강남역_역삼역 = new Section(강남역, 역삼역, 6);
            Section 역삼역_선릉역 = new Section(역삼역, 선릉역, 6);

            // when & then
            assertAll("addInvalidDistance",
                    () -> assertThatThrownBy(() -> sections.add(강남역_역삼역))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("현재 구간보다 긴 구간을 추가할 수 없습니다."),
                    () -> assertThatThrownBy(() -> sections.add(역삼역_선릉역))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("현재 구간보다 긴 구간을 추가할 수 없습니다.")
            );

        }
    }

    @DisplayName("Sections에서 역을 정상적으로 삭제한다.")
    @Nested
    class removeTest {
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 잠실역 = new Station(3L, "잠실역");

        Section 강남역_선릉역 = new Section(강남역, 선릉역, 5, 1);
        Section 선릉역_잠실역 = new Section(선릉역, 잠실역, 5, 2);

        Sections sections;

        @BeforeEach
        void setUp() {
            sections = new Sections(new ArrayList<>(List.of(강남역_선릉역, 선릉역_잠실역)));
        }

        @DisplayName("Sections에 지울 역이 되는 구간이 존재하지 않을 경우 NoSuchElementException이 발생한다.")
        @Test
        void remove_InvalidStation() {
            // given
            Station 판교역 = new Station(4L, "판교역");

            // when & then
            assertThatThrownBy(() -> sections.remove(판교역))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("삭제할 구간이 존재하지 않습니다.");
        }

        @DisplayName("Sections에서 하행 종점역을 삭제한다.")
        @Test
        void removeFinalSection_DownDirection() {
            // when
            sections.remove(강남역);

            // then
            assertThat(sections.getSections())
                    .contains(선릉역_잠실역);
        }

        @DisplayName("Sections에서 상행 종점역을 삭제한다.")
        @Test
        void removeFinalSection_UpDirection() {
            // when
            sections.remove(잠실역);

            // then
            assertThat(sections.getSections())
                    .contains(강남역_선릉역);
        }

        @DisplayName("Sections에서 중간역을 삭제한다.")
        @Test
        void remove() {
            // when
            sections.remove(선릉역);

            // then
            Section 강남역_잠실역 = new Section(강남역, 잠실역, 10, 1);
            assertThat(sections.getSections())
                    .contains(강남역_잠실역);
        }
    }

    @DisplayName("Sections가 모두 삭제된다.")
    @Test
    void clear() {
        // given
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");

        Section 강남역_선릉역 = new Section(강남역, 선릉역, 5);

        Sections sections = new Sections(new ArrayList<>(List.of(강남역_선릉역)));

        // when
        sections.clear();

        // then
        assertThat(sections.size()).isEqualTo(0);
    }
}
