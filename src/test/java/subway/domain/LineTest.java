package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @Test
    @DisplayName("유효한 Section 하나를 입력받으면 노선이 정상적으로 생성된다.")
    void constructor_success() {
        //given
        Station upStation = new Station("상행역");
        Station downStation = new Station( "하행역");
        Distance distance = new Distance(10);
        Section section = new Section(upStation, downStation, distance);

        //when, then
        assertThatCode(() -> new Line("2호선", "초록색", section)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상행 혹은 하행역을 입력받지 않으면 예외발생")
    void constructor_fail_station_null() {
        //given
        Station upStation = new Station("상행역");
        Station downStation =Station.emptyStation;
        Distance distance = new Distance(10);
        Section section=new Section(upStation, downStation, distance);
        //when, then
        assertThatThrownBy(() ->new Line("2호선", "초록색", section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역 혹은 하행역을 입력하지 않았습니다.");
    }

}
