package subway.business.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixture.FixtureForLineTest.station1;
import static subway.fixture.FixtureForLineTest.station2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.business.domain.transfer.Transfer;

class TransferTest {

    @DisplayName("ID 없이 역 2개만 받아, ID가 빠른 순서로 저장한다.")
    @Test
    void shouldOrderStationsByIdWhenCreateWithoutId() {
        Station stationOfId1 = station1();
        Station stationOfId2 = station2();

        Transfer transfer = new Transfer(stationOfId2, stationOfId1);

        assertThat(transfer.getFirstStation()).isEqualTo(stationOfId1);
        assertThat(transfer.getLastStation()).isEqualTo(stationOfId2);
    }

    @DisplayName("ID와 역 2개만 받아, ID가 빠른 순서로 저장한다.")
    @Test
    void shouldOrderStationsByIdWhenCreate() {
        Station stationOfId1 = station1();
        Station stationOfId2 = station2();

        Transfer transfer = new Transfer(1L, stationOfId2, stationOfId1);

        assertThat(transfer.getFirstStation()).isEqualTo(stationOfId1);
        assertThat(transfer.getLastStation()).isEqualTo(stationOfId2);
    }

    @DisplayName("같은 역 2개로 환승을 생성하면 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenCreateWithSameStations() {
        assertThatThrownBy(() -> new Transfer(1L, station1(), station1()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 역끼리 환승 구간으로 지정할 수 없습니다. "
                        + "(입력한 역 : 잠실역)");
    }
}
