package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.exception.section.InvalidSectionDirectionException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 출발지와_도착지가_같으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Section("A", "A", 10))
                .isInstanceOf(InvalidSectionDirectionException.class)
                .hasMessage("출발지와 도착지는 같을 수 없습니다");
    }

    @ValueSource(strings = {"강남역", "역삼역"})
    @ParameterizedTest
    void 입력받은_지하철역과_이름이_같은_source_또는_target이_존재하면_true를_반환한다(String value) {
        // given
        Section section = new Section("강남역", "역삼역", 10);

        // when
        Station sameNameStation = new Station(value);
        boolean isAnySame = section.contains(sameNameStation);

        // then
        assertThat(isAnySame).isTrue();
    }

    @Test
    void 입력받은_지하철역과_구간의_모든_이름과_다르면_false를_반환하다() {
        // given
        Section section = new Section("강남역", "역삼역", 10);

        // when
        Station sameNameStation = new Station("잠실역");
        boolean isAnySame = section.contains(sameNameStation);

        // then
        assertThat(isAnySame).isFalse();
    }
}
