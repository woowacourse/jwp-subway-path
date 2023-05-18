package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.vo.Distance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SectionsTest {
    private static final Distance 거리1 = Distance.from(1);
    private static final Distance 거리10 = Distance.from(10);
    private static final Distance 거리20 = Distance.from(20);


    @Test
    void 비어있는_구간_목록에_새로운_구간을_추가할_수_있다() {
        // given
        final Station 잠실역 = new Station("잠실");
        final Station 잠실새내역 = new Station("잠실새내");
        final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);

        final Sections 구간_목록 = Sections.from(List.of());

        // expect
        assertDoesNotThrow(() -> 구간_목록.addSection(잠실역_잠실새내역_구간));
    }

    @Test
    void 기존_구간과_같은_새로운_구간을_추가할_경우_예외가_발생한다() {
        // given
        final Station 잠실역 = new Station("잠실");
        final Station 잠실새내역 = new Station("잠실새내");
        final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);

        final Sections 구간_목록 = Sections.from(List.of(잠실역_잠실새내역_구간));

        // expect
        assertThatThrownBy(() -> 구간_목록.addSection(잠실역_잠실새내역_구간))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간이 하나 이상 존재하는 노선에 새로운 구간을 등록할 때 기준이 되는 지하철역이")
    @Nested
    class SectionAddValidation {

        @Test
        void 존재하지않는_경우_예외가_발생한다() {
            // given
            final Station 잠실역 = new Station("잠실");
            final Station 잠실새내역 = new Station("잠실새내");
            final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);

            final Station 창동역 = new Station("창동");
            final Station 녹천역 = new Station("녹천");
            final Section 창동역_녹천역_구간 = new Section(거리10, true, 창동역, 녹천역);

            final Sections 구간_목록 = Sections.from(List.of(잠실역_잠실새내역_구간));

            // expect
            assertThatThrownBy(() -> 구간_목록.addSection(창동역_녹천역_구간))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하는_경우_등록_성공한다() {
            // given
            final Station 잠실역 = new Station("잠실");
            final Station 잠실새내역 = new Station("잠실새내");
            final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);

            final Station 창동역 = new Station("창동");
            final Section 잠실새내역_창동역_구간 = new Section(거리10, false, 잠실새내역, 창동역);

            final Sections 구간_목록 = Sections.from(List.of(잠실역_잠실새내역_구간));

            // expect
            assertDoesNotThrow(() -> 구간_목록.addSection(잠실새내역_창동역_구간));
        }

    }

    @DisplayName("기존 구간의 상행역이 기준이 되고")
    @Nested
    class OldSectionWithOldUpStation {

        @DisplayName("새로운 구간의 상행역과 같을 경우")
        @Nested
        class NewSectionWithNewDownStation {

            @Test
            void 새로운_구간의_길이가_기존_구간의_길이보다_클_경우_예외가_발생한다() {
                // given
                final Station 기존_상행역 = new Station("기존_상행역");
                final Station 기존_하행역 = new Station("기존_하행역");
                final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);

                final Station 새로운_하행역 = new Station("새로운_하행역");
                final Section 새로운_구간 = new Section(거리20, false, 기존_상행역, 새로운_하행역);

                final Sections 구간_목록 = Sections.from(List.of(기존_구간));

                // expect
                assertThatThrownBy(() -> 구간_목록.addSection(새로운_구간))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 새로운_구간의_길이가_기존_구간의_길이가_같을_경우_예외가_발생한다() {
                // given
                final Station 기존_상행역 = new Station("기존_상행역");
                final Station 기존_하행역 = new Station("기존_하행역");
                final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);

                final Station 새로운_하행역 = new Station("새로운_하행역");
                final Section 새로운_구간 = new Section(거리10, false, 기존_상행역, 새로운_하행역);

                final Sections 구간_목록 = Sections.from(List.of(기존_구간));

                // expect
                assertThatThrownBy(() -> 구간_목록.addSection(새로운_구간))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("새로운 구간의 하행역과 같을 경우")
        @Nested
        class NewSectionWithNewUpStation {

            @Test
            void 새로운_구간의_길이가_0보다_클_경우_등록_성공한다() {
                // given
                final Station 기존_상행역 = new Station("기존_상행역");
                final Station 기존_하행역 = new Station("기존_하행역");
                final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);

                final Station 새로운_상행역 = new Station("새로운_상행역");
                final Section 새로운_구간 = new Section(거리1, false, 새로운_상행역, 기존_하행역);

                final Sections 구간_목록 = Sections.from(List.of(기존_구간));

                // expect
                assertDoesNotThrow(() -> 구간_목록.addSection(새로운_구간));
            }
        }
    }

    @DisplayName("기존 구간의 하행역이 기준이 되고")
    @Nested
    class OldSectionWithOldDownStation {

        @DisplayName("새로운 구간의 상행역과 같을 경우")
        @Nested
        class NewSectionWithNewUpStation {

            @Test
            void 새로운_구간의_길이가_0보다_클_경우_등록_성공한다() {
                // given
                final Station 기존_상행역 = new Station("기존_상행역");
                final Station 기존_하행역 = new Station("기존_하행역");
                final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);

                final Station 새로운_하행역 = new Station("새로운_하행역");
                final Section 새로운_구간 = new Section(거리1, false, 기존_상행역, 새로운_하행역);

                final Sections 구간_목록 = Sections.from(List.of(기존_구간));

                // expect
                assertDoesNotThrow(() -> 구간_목록.addSection(새로운_구간));
            }
        }

        @DisplayName("새로운 구간의 하행역과 같을 경우")
        @Nested
        class NewSectionWithNewDownStation {

            @Test
            void 새로운_구간의_길이가_기존_구간의_길이보다_클_경우_예외가_발생한다() {
                // given
                final Station 기존_상행역 = new Station("기존_상행역");
                final Station 기존_하행역 = new Station("기존_하행역");
                final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);

                final Station 새로운_상행역 = new Station("새로운_상행역");
                final Section 새로운_구간 = new Section(거리20, false, 새로운_상행역, 기존_하행역);

                final Sections 구간_목록 = Sections.from(List.of(기존_구간));

                // expect
                assertThatThrownBy(() -> 구간_목록.addSection(새로운_구간))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 새로운_구간의_길이가_기존_구간의_길이가_같을_경우_예외가_발생한다() {
                // given
                final Distance 거리10 = Distance.from(10);

                final Station 기존_상행역 = new Station("기존_상행역");
                final Station 기존_하행역 = new Station("기존_하행역");
                final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);

                final Station 새로운_상행역 = new Station("새로운_상행역");
                final Section 새로운_구간 = new Section(거리10, false, 새로운_상행역, 기존_하행역);

                final Sections 구간_목록 = Sections.from(List.of(기존_구간));

                // expect
                assertThatThrownBy(() -> 구간_목록.addSection(새로운_구간))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Test
    void 상행종점역을_삭제한다() {
        // given
        final Station 잠실역 = new Station("잠실");
        final Station 잠실새내역 = new Station("잠실새내");
        final Station 창동역 = new Station("창동");
        final Station 녹천역 = new Station("녹천");

        final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);
        final Section 잠실새내역_창동역_구간 = new Section(거리10, false, 잠실새내역, 창동역);
        final Section 창동역_녹천역_구간 = new Section(거리10, false, 창동역, 녹천역);

        final Sections 구간_목록 = Sections.from(List.of(잠실역_잠실새내역_구간, 잠실새내역_창동역_구간, 창동역_녹천역_구간));

        // when
        구간_목록.removeStation(잠실역);

        final List<Section> 구간_목록들 = 구간_목록.getOldSections();

        // then
        assertThat(구간_목록들)
                .containsExactly(
                        new Section(거리10, true, 잠실새내역, 창동역),
                        new Section(거리10, false, 창동역, 녹천역)
                );
    }

    @Test
    void 중간역을_삭제한다() {
        // given
        final Station 잠실역 = new Station("잠실");
        final Station 잠실새내역 = new Station("잠실새내");
        final Station 창동역 = new Station("창동");
        final Station 녹천역 = new Station("녹천");

        final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);
        final Section 잠실새내역_창동역_구간 = new Section(거리10, false, 잠실새내역, 창동역);
        final Section 창동역_녹천역_구간 = new Section(거리10, false, 창동역, 녹천역);

        final Sections 구간_목록 = Sections.from(List.of(잠실역_잠실새내역_구간, 잠실새내역_창동역_구간, 창동역_녹천역_구간));

        // when
        구간_목록.removeStation(잠실새내역);

        final List<Section> 구간_목록들 = 구간_목록.getOldSections();

        // then
        assertThat(구간_목록들)
                .containsExactly(
                        new Section(거리20, true, 잠실역, 창동역),
                        new Section(거리10, false, 창동역, 녹천역)
                );
    }

    @Test
    void 구간을_구성하는_역을_모은다() {
        // given
        final Station 잠실역 = new Station("잠실");
        final Station 잠실새내역 = new Station("잠실새내");
        final Station 창동역 = new Station("창동");
        final Station 녹천역 = new Station("녹천");

        final Section 잠실역_잠실새내역_구간 = new Section(거리10, true, 잠실역, 잠실새내역);
        final Section 잠실새내역_창동역_구간 = new Section(거리10, false, 잠실새내역, 창동역);
        final Section 창동역_녹천역_구간 = new Section(거리10, false, 창동역, 녹천역);

        final Sections 구간_목록 = Sections.from(List.of(잠실역_잠실새내역_구간, 잠실새내역_창동역_구간, 창동역_녹천역_구간));

        // when
        final List<Station> 역_목록 = 구간_목록.collectAllStations();

        // then
        assertThat(역_목록)
                .contains(잠실역, 잠실새내역, 창동역, 녹천역);
    }
}
