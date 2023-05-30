package subway.domain.section;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixtures.domain.DistanceFixture.FIVE_DISTANCE;
import static subway.fixtures.domain.SectionFixture.JAMSIL_UP_SADANG_MID_10;
import static subway.fixtures.domain.SectionFixture.JAMSIL_UP_SADANG_MID_5;
import static subway.fixtures.domain.SectionFixture.JAMSIL_UP_SEOLLEUNG_DOWN_10;
import static subway.fixtures.domain.SectionFixture.JAMSIL_UP_SEOLLEUNG_MID_10;
import static subway.fixtures.domain.SectionFixture.SADAND_MID_SEOLLEUNG_DOWN_5;
import static subway.fixtures.domain.SectionFixture.SADANG_MID_HONGDAE_DOWN_10;
import static subway.fixtures.domain.SectionFixture.SADANG_MID_SEOLLEUNG_DOWN_10;
import static subway.fixtures.domain.SectionFixture.SADANG_MID_SEOLLEUNG_DOWN_5;
import static subway.fixtures.domain.SectionFixture.SEOLLEUNG_MID_SADANG_MID_10;
import static subway.fixtures.domain.StationFixture.JAMSIL;
import static subway.fixtures.domain.StationFixture.JAMSIL_UP;
import static subway.fixtures.domain.StationFixture.SADANG_MID;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG_DOWN;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 구간에_같은_역이_들어오면_예외가_들어오는지_확인한다() {
        assertThatThrownBy(() -> Section.of(JAMSIL, JAMSIL, Distance.from(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간을 등록하는 두 역이 같을 수 없습니다.");
    }

    @Test
    void 구간이_종점역을_가지고_있는지_확인한다() {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(JAMSIL_UP_SEOLLEUNG_MID_10.isEndSection()).isTrue();
            softAssertions.assertThat(SEOLLEUNG_MID_SADANG_MID_10.isEndSection()).isFalse();
            softAssertions.assertThat(SADANG_MID_HONGDAE_DOWN_10.isEndSection()).isTrue();
        });
    }

    @Nested
    class CreateSection_Test {

        @Nested
        class A역_C역_구간의_중간에_A역_B역_구간을_추가할_때 {

            @Test
            void A역과_B역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section upSection = JAMSIL_UP_SADANG_MID_5.createUpSection(JAMSIL_UP_SEOLLEUNG_DOWN_10);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(upSection.getUpStation()).isEqualTo(JAMSIL_UP);
                    softAssertions.assertThat(upSection.getDownStation()).isEqualTo(SADANG_MID);
                    softAssertions.assertThat(upSection.getDistance()).isEqualTo(FIVE_DISTANCE);
                });
            }

            @Test
            void B역과_C역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section newDownSection = SADAND_MID_SEOLLEUNG_DOWN_5.createDownSection(JAMSIL_UP_SEOLLEUNG_DOWN_10);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newDownSection.getUpStation()).isEqualTo(SADANG_MID);
                    softAssertions.assertThat(newDownSection.getDownStation()).isEqualTo(SEOLLEUNG_DOWN);
                    softAssertions.assertThat(newDownSection.getDistance()).isEqualTo(FIVE_DISTANCE);
                });
            }

            @Test
            void A역_B역_구간의_길이가_A역_C역_구간의_길이보다_크거나_같으면_에러를_반환한다() {
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(() -> JAMSIL_UP_SADANG_MID_10.createDownSection(JAMSIL_UP_SEOLLEUNG_DOWN_10))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("등록하려는 구간의 길이는 원본 구간의 길이 미만이어야 합니다.");
                });
            }
        }

        @Nested
        class A역_C역_구간의_중간에_B역_C역_구간을_추가할_때 {

            @Test
            void A역과_B역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section newUpSection = SADANG_MID_SEOLLEUNG_DOWN_5.createUpSection(JAMSIL_UP_SEOLLEUNG_DOWN_10);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newUpSection.getUpStation()).isEqualTo(JAMSIL_UP);
                    softAssertions.assertThat(newUpSection.getDownStation()).isEqualTo(SADANG_MID);
                    softAssertions.assertThat(newUpSection.getDistance()).isEqualTo(FIVE_DISTANCE);
                });
            }

            @Test
            void B역과_C역을_가진_구간이_제대로_생성되는지_확인한다() {
                final Section newDownSection = SADANG_MID_SEOLLEUNG_DOWN_5.createDownSection(JAMSIL_UP_SEOLLEUNG_MID_10);

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(newDownSection.getUpStation()).isEqualTo(SADANG_MID);
                    softAssertions.assertThat(newDownSection.getDownStation()).isEqualTo(SEOLLEUNG_DOWN);
                    softAssertions.assertThat(newDownSection.getDistance()).isEqualTo(FIVE_DISTANCE);
                });
            }

            @Test
            void B역_C역_구간의_길이가_A역_C역_구간의_길이보다_크거나_같으면_에러를_반환한다() {
                assertThatThrownBy(() -> SADANG_MID_SEOLLEUNG_DOWN_10.createUpSection(JAMSIL_UP_SEOLLEUNG_DOWN_10))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("등록하려는 구간의 길이는 원본 구간의 길이 미만이어야 합니다.");
            }
        }
    }

    @Test
    void 특정_역이_특정_구간에_존재하는지_확인한다() {
        final Section section = JAMSIL_UP_SEOLLEUNG_DOWN_10;

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(section.contains(JAMSIL_UP)).isTrue();
            softAssertions.assertThat(section.contains(SEOLLEUNG_DOWN)).isTrue();
            softAssertions.assertThat(section.contains(SADANG_MID)).isFalse();
        });
    }

    @Test
    void 구간에_저장된_역을_제외한_다른_역을_제대로_반환하는지_확인한다() {
        final Section section = JAMSIL_UP_SEOLLEUNG_DOWN_10;

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(section.findOtherStation(JAMSIL_UP)).isEqualTo(SEOLLEUNG_DOWN);
            softAssertions.assertThat(section.findOtherStation(SEOLLEUNG_DOWN)).isEqualTo(JAMSIL_UP);
        });
    }

    @Test
    void 구간에_속한_역의_다른_역을_제대로_반환하는지_확인하는데_구간이_아닌_다른_역을_입력하면_에러를_반환한다() {
        final Section section = JAMSIL_UP_SEOLLEUNG_DOWN_10;

        assertThatThrownBy(() -> section.findOtherStation(SADANG_MID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 구간에 입력한 역이 존재하지 않습니다.");
    }
}
