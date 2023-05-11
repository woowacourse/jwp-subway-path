package subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {

    @BeforeEach
    void setUp() {
        Station.clear();
    }

    @DisplayName("새로운 역을 등록한다.")
    @Test
    void registerSuccess() {
        //given
        //when
        final Station 잠실역 = Station.register("잠실역");

        //then
        assertThat(잠실역.getName()).isEqualTo("잠실역");
    }

    @DisplayName("이미 등록된 역을 등록할 경우 오류를 던진다.")
    @Test
    void registerFail() {
        //given
        Station.register("잠실역");

        //when
        //then
        assertThatThrownBy(() -> Station.register("잠실역"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 이름을 가진 역을 조회한다.")
    @Test
    void getSuccess() {
        //given
        Station.register("잠실역");

        //when
        final Station 잠실역 = Station.get("잠실역");

        //then
        assertThat(잠실역.getName()).isEqualTo("잠실역");
    }

    @DisplayName("없는 역을 조회할시 오류를 던진다.")
    @Test
    void getFail() {
        //given
        //when
        //then
        assertThatThrownBy(() -> Station.get("잠실역"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 이름을 가진 역을 삭제한다.")
    @Test
    void removeSuccess() {
        //given
        Station.register("잠실역");

        //when
        Station.remove("잠실역");

        //then
        assertThatThrownBy(() -> Station.get("잠실역"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 역을 삭제시 오류를 던진다.")
    @Test
    void removeFail() {
        //given
        //when
        //then
        assertThatThrownBy(() -> Station.remove("잠실역"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
