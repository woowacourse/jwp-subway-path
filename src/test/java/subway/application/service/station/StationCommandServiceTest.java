package subway.application.service.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.port.in.section.RemoveStationFromLineUseCase;
import subway.application.port.in.station.dto.command.CreateStationCommand;
import subway.application.port.in.station.dto.command.UpdateStationCommand;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.station.LoadStationPort;
import subway.application.port.out.station.PersistStationPort;
import subway.application.service.exception.NoSuchStationException;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.fixture.StationFixture.역삼역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StationCommandServiceTest {

    @Mock
    private LoadStationPort loadStationPort;

    @Mock
    private PersistStationPort persistStationPort;

    @Mock
    private LoadLinePort loadLinePort;

    @Mock
    private RemoveStationFromLineUseCase removeStationFromLineUseCase;

    @InjectMocks
    private StationCommandService stationCommandService;

    @Nested
    class 역_생성시_ {

        private final String name = "역삼역";
        private final CreateStationCommand command = new CreateStationCommand(name);

        @Test
        void 이미_존재하는_이름이면_예외() {
            // given
            given(loadStationPort.findByName(name))
                    .willReturn(Optional.of(역삼역.STATION));

            // when then
            assertThatThrownBy(() -> stationCommandService.createStation(command))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("기존 역과 중복된 이름입니다.");
        }

        @Test
        void 성공() {
            // given
            given(loadStationPort.findByName(name))
                    .willReturn(Optional.empty());

            // when
            long stationId = stationCommandService.createStation(command);

            // then
            assertThat(stationId).isNotNull();
        }
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
