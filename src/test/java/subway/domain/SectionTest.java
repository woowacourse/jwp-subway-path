package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalInputForDomainException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SectionTest {

    public static final Station UP_STATION = new Station("잠실나루");
    public static final Station DOWN_STATION = new Station("잠실새내");
    public static final Distance DISTANCE = new Distance(10);


    @DisplayName("생성한다")
    @Test
    void 생성한다() {
        assertDoesNotThrow(() -> new Section(UP_STATION, DOWN_STATION, DISTANCE));
    }

    @DisplayName("upStation이 null이면 예외를 발생한다.")
    @Test
    void upStation이_null이면_예외를_발생한다() {

        assertThatThrownBy(() -> new Section(null, DOWN_STATION, DISTANCE))
                .isInstanceOf(IllegalInputForDomainException.class);
    }

    @DisplayName("downStation이 null이면 예외를 발생한다.")
    @Test
    void downStation이_null이면_예외를_발생한다() {

        assertThatThrownBy(() -> new Section(UP_STATION, null, DISTANCE))
                .isInstanceOf(IllegalInputForDomainException.class);
    }

    @DisplayName("distance가 null이면 예외를 발생한다.")
    @Test
    void distance가_null이면_예외를_발생한다() {

        assertThatThrownBy(() -> new Section(UP_STATION, DOWN_STATION, null))
                .isInstanceOf(IllegalInputForDomainException.class);
    }

    @DisplayName("구간을 입력 받아 나눠 List<Section>을 반환한다")
    @Test
    void 구간을_나눈다() {
        //given
        Section section = new Section(UP_STATION, DOWN_STATION, DISTANCE);
        Station jamsil = new Station("잠실");
        Section otherSection = new Section(UP_STATION, jamsil, new Distance(7));
        //when
        List<Section> divide = section.divide(otherSection);
        //then
        assertThat(divide).contains(otherSection, new Section(jamsil, DOWN_STATION, new Distance(3)));
    }

    @DisplayName("두 구간을 병합한다.")
    @Test
    void mergeWith() {
        //given
        Section upSection = new Section(UP_STATION, DOWN_STATION, DISTANCE);
        Section downSection = new Section(DOWN_STATION, new Station("구의"), new Distance(10));
        //when
        Section mergedSection = upSection.mergeWith(downSection);
        //then
        assertThat(mergedSection)
                .isEqualTo(new Section(new Station("잠실나루"), new Station("구의"), new Distance(20)));
    }
}
