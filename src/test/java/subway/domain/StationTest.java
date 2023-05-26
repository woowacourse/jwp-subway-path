package subway.domain;

import org.junit.jupiter.api.Test;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @Test
    void 역_이름이_같으면_동일한_역이다() {
        Station jamsil1 = new Station("잠실");
        Station jamsil2 = new Station("잠실");

        assertThat(jamsil1.isSame(jamsil2)).isTrue();
    }

    @Test
    void 노선에_역_추가시_이름이_다른지_확인한다() {
        Station jamsil = new Station("잠실");
        Station seonleung = new Station("선릉");

        assertThat(jamsil.isSame(seonleung)).isFalse();
    }
}
