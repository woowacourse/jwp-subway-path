//package subway.domain;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThatCode;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//class StationTest {
//
//    @Test
//    @DisplayName("양 역의 이름이 같다면 예외를 발생시킨다.")
//    void validateSameStations_fail() {
//        //given
//        String name = "상행역";
//        Distance distance = new Distance(10);
//        Station next = new Station(name);
//
//
//        //when, then
//        assertThatThrownBy(() -> new Station(name, next, distance))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("상행역과 하행역은 같은 이름을 가질 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("올바른 값이 입력되면 정상적으로 생성된다")
//    void constructor_success() {
//        //given
//        String name = "상행역";
//        Distance distance = new Distance(10);
//        Station next = new Station("하행역");
//
//
//        //when, then
//        assertThatCode(() -> new Station(name, next, distance))
//                .doesNotThrowAnyException();
//    }
//
//    @Test
//    @DisplayName("isDownEndStation()를 호출했을 때 다음역이 emptyStation이라면 true를 반환한다.")
//    void isIncludeEmptyStation_true() {
//        //given
//        String name = "상행역";
//        Station next = Station.emptyStation;
//        Distance distance = new Distance(10);
//        Station station = new Station(name, next, distance);
//
//        //when
//        boolean actual = station.isDownEndStation();
//
//        // then
//        assertThat(actual).isTrue();
//    }
//
//    //TODO: isIncludeEmptyStation() false검증
//
//    @Test
//    @DisplayName("isSameName()를 호출했을 때 Station이 특정 이름을 가지고 있다면 true를 반환한다.")
//    void isSameName_true() {
//        //given
//        String name = "강남역";
//        Station station = new Station(name);
//
//        //when
//        boolean actual = station.isSameName(name);
//
//        //then
//        Assertions.assertThat(actual).isTrue();
//    }
//
//    @Test
//    @DisplayName("isSameName()를 호출했을 때 Station이 특정 이름을 가지고 있지 않다면 false를 반환한다.")
//    void isSameName_false() {
//        //given
//        String name = "강남역";
//        String differentName = "역삼역";
//        Station station = new Station(name);
//
//        //when
//        boolean actual = station.isSameName(differentName);
//
//        //then
//        Assertions.assertThat(actual).isFalse();
//    }
//}
