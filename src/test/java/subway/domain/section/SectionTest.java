package subway.domain.section;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Position;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    private final Station UP_END_STATION = Station.of(1L, "잠실역", Position.UP);
    private final Station MID_STATION_ONE = Station.of(2L, "선릉역", Position.MID);
    private final Station MID_STATION_TWO = Station.of(3L, "사당역", Position.MID);
    private final Station DOWN_END_STATION = Station.of(4L, "삼성역", Position.DOWN);

    @Test
    void 구간에_같은_역이_들어오면_예외가_들어오는지_확인한다() {
        assertThatThrownBy(() -> Section.of(MID_STATION_ONE, MID_STATION_ONE, Distance.from(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간을 등록하는 두 역이 같을 수 없습니다.");
    }

    @Test
    void 구간이_종점역을_가지고_있는지_확인한다() {
        final Section upEndSection = Section.of(UP_END_STATION, MID_STATION_ONE, Distance.from(10));
        final Section downEndSection = Section.of(MID_STATION_ONE, DOWN_END_STATION, Distance.from(10));
        final Section midSection = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(10));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(upEndSection.isEndSection()).isTrue();
            softAssertions.assertThat(downEndSection.isEndSection()).isTrue();
            softAssertions.assertThat(midSection.isEndSection()).isFalse();
        });
    }

    @Nested
    class CreateSection_Test {

        @Nested
        class A역_C역_구간의_중간에_A역_B역_구간을_추가할_때 {

            @Test
            void A역과_B역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section originSection = Section.of(UP_END_STATION, MID_STATION_TWO, Distance.from(10));
                final Section newMidSection = Section.of(UP_END_STATION, MID_STATION_ONE, Distance.from(3));
                final Section newUpSection = newMidSection.createUpSection(originSection);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newUpSection.getUpStation()).isEqualTo(UP_END_STATION);
                    softAssertions.assertThat(newUpSection.getDownStation()).isEqualTo(MID_STATION_ONE);
                    softAssertions.assertThat(newUpSection.getDistance().getDistance()).isEqualTo(3);
                });
            }

            @Test
            void B역과_C역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section originSection = Section.of(UP_END_STATION, MID_STATION_TWO, Distance.from(10));
                final Section newMidSection = Section.of(UP_END_STATION, MID_STATION_ONE, Distance.from(3));
                final Section newDownSection = newMidSection.createDownSection(originSection);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newDownSection.getUpStation().getName()).isEqualTo(MID_STATION_ONE.getName());
                    softAssertions.assertThat(newDownSection.getDownStation()).isEqualTo(MID_STATION_TWO);
                    softAssertions.assertThat(newDownSection.getDistance().getDistance()).isEqualTo(7);
                });
            }

            @Test
            void A역_B역_구간의_길이가_A역_C역_구간의_길이보다_크거나_같으면_에러를_반환한다() {
                final Section originSection = Section.of(UP_END_STATION, MID_STATION_TWO, Distance.from(10));
                final Section newMidSection = Section.of(UP_END_STATION, MID_STATION_ONE, Distance.from(10));

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(() -> newMidSection.createDownSection(originSection))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("등록하려는 구간의 길이는 원본 구간의 길이 미만이어야 합니다.");
                });
            }
        }

        @Nested
        class A역_C역_구간의_중간에_B역_C역_구간을_추가할_때 {

            @Test
            void A역과_B역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section originSection = Section.of(UP_END_STATION, MID_STATION_TWO, Distance.from(10));
                final Section newMidSection = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(3));
                final Section newUpSection = newMidSection.createUpSection(originSection);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newUpSection.getUpStation()).isEqualTo(UP_END_STATION);
                    softAssertions.assertThat(newUpSection.getDownStation()).isEqualTo(MID_STATION_ONE);
                    softAssertions.assertThat(newUpSection.getDistance().getDistance()).isEqualTo(7);
                });
            }

            @Test
            void B역과_C역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section originSection = Section.of(UP_END_STATION, MID_STATION_TWO, Distance.from(10));
                final Section newMidSection = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(3));
                final Section newDownSection = newMidSection.createDownSection(originSection);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newDownSection.getUpStation().getName()).isEqualTo(MID_STATION_ONE.getName());
                    softAssertions.assertThat(newDownSection.getDownStation()).isEqualTo(MID_STATION_TWO);
                    softAssertions.assertThat(newDownSection.getDistance().getDistance()).isEqualTo(3);
                });
            }

            @Test
            void B역_C역_구간의_길이가_A역_C역_구간의_길이보다_크거나_같으면_에러를_반환한다() {
                final Section originSection = Section.of(UP_END_STATION, MID_STATION_TWO, Distance.from(10));
                final Section newMidSection = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(10));

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(() -> newMidSection.createUpSection(originSection))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("등록하려는 구간의 길이는 원본 구간의 길이 미만이어야 합니다.");
                });
            }
        }
    }

    @Test
    void 특정_역이_특정_구간에_존재하는지_확인한다() {
        final Section section = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(10));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(section.contains(MID_STATION_ONE)).isTrue();
            softAssertions.assertThat(section.contains(MID_STATION_TWO)).isTrue();
            softAssertions.assertThat(section.contains(UP_END_STATION)).isFalse();
        });
    }

    @Test
    void 구간에_저장된_역을_제외한_다른_역을_제대로_반환하는지_확인한다() {
        final Section section = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(10));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(section.findOtherStation(MID_STATION_ONE)).isEqualTo(MID_STATION_TWO);
            softAssertions.assertThat(section.findOtherStation(MID_STATION_TWO)).isEqualTo(MID_STATION_ONE);
        });
    }

    @Test
    void 구간에_저장된_역을_다른_역을_제대로_반환하는지_확인하는데_구간이_아닌_다른_역을_입력하면_에러를_반환한다() {
        final Section section = Section.of(MID_STATION_ONE, MID_STATION_TWO, Distance.from(10));

        assertThatThrownBy(() -> section.findOtherStation(UP_END_STATION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 구간에 입력한 역이 존재하지 않습니다.");
    }
}
