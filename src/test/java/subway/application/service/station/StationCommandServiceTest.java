package subway.application.service.station;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.application.port.in.station.dto.command.UpdateStationCommand;
import subway.application.port.out.station.LoadStationPort;
import subway.application.port.out.station.PersistStationPort;
import subway.common.exception.NoSuchStationException;
import subway.fixture.StationFixture.역삼역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationCommandServiceTest {

    private LoadStationPort loadStationPort;
    private PersistStationPort persistStationPort;
    private StationCommandService stationCommandService;

    @BeforeEach
    void setUp() {
        loadStationPort = mock(LoadStationPort.class);
        persistStationPort = mock(PersistStationPort.class);
        stationCommandService = new StationCommandService(loadStationPort, persistStationPort);
    }

    @Nested
    class 역_갱신시_ {

        private final long stationId = 1L;
        private final UpdateStationCommand command = new UpdateStationCommand(stationId, "역삼역");

        @Test
        void 아이디에_해당하는_역이_존재하지_않으면_예외() {
            // given
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> stationCommandService.updateStation(command))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 성공() {
            // given
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.of(역삼역.STATION));

            // when then
            assertThatNoException().isThrownBy(() -> stationCommandService.updateStation(command));
        }
    }

    @Nested
    class 역_삭제시_ {

        private final long stationId = 1L;


        @Test
        void 아이디에_해당하는_역이_존재하지_않으면_예외() {
            // given
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> stationCommandService.deleteStation(stationId))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 성공() {
            // given
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.of(역삼역.STATION));

            // when then
            assertThatNoException().isThrownBy(() -> stationCommandService.deleteStation(stationId));
        }
    }
}
