package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SectionTest {

    @Test
    @DisplayName("isIncludeEmptyStation()를 호출했을 때 양 역 중 하나 이상이 emptyStation이라면 false를 반환한다.")
    void isIncludeEmptyStation_false(){
        //given
        Station upStation = new Station("상행역");
        Station downStation = Station.emptyStation;
        Distance distance = new Distance(10);
        Section section = new Section(upStation, downStation, distance);

        //when
        boolean actual = section.isIncludeEmptyStation();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("validateSameStations()를 호출했을 때 양 역의 이름이 같다면 예외를 발생시킨다.")
    void validateSameStations_fail(){
        //given
        Station upStation = new Station("상행역");
        Station downStation = new Station("상행역");
        Distance distance = new Distance(10);

        //when, then
        assertThatThrownBy(()->new Section(upStation, downStation, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역은 같은 이름을 가질 수 없습니다.");

    }

}
