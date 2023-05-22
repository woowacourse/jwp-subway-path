package subway.application.service.section;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.application.port.in.section.dto.command.AddStationToLineCommand;
import subway.application.port.in.section.dto.command.RemoveStationFromLineCommand;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.line.PersistLinePort;
import subway.application.port.out.station.LoadStationPort;
import subway.common.exception.NoSuchLineException;
import subway.common.exception.NoSuchStationException;
import subway.domain.line.Line;
import subway.fixture.LineFixture.이호선;
import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionCommandServiceTest {

    private LoadLinePort loadLinePort;
    private PersistLinePort persistLinePort;
    private LoadStationPort loadStationPort;
    private SectionCommandService sectionCommandService;

    @BeforeEach
    void setUp() {
        loadLinePort = mock(LoadLinePort.class);
        persistLinePort = mock(PersistLinePort.class);
        loadStationPort = mock(LoadStationPort.class);
        sectionCommandService = new SectionCommandService(loadLinePort, persistLinePort, loadStationPort);
    }

    @Nested
    class 노선에_역_추가시_ {

        private final long lineId = 1L;
        private final long upStationId = 2L;
        private final long downStationId = 3L;
        private final AddStationToLineCommand command = new AddStationToLineCommand(lineId, upStationId, downStationId,
                3);

        @BeforeEach
        void setUp() {
            given(loadLinePort.findById(lineId))
                    .willReturn(Optional.of(이호선.LINE));
            given(loadStationPort.findById(upStationId))
                    .willReturn(Optional.of(역삼역.STATION));
            given(loadStationPort.findById(downStationId))
                    .willReturn(Optional.of(삼성역.STATION));
        }

        @Test
        void 라인아이디에_해당하는_라인이_없을시_예외() {
            // given
            given(loadLinePort.findById(lineId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> sectionCommandService.addStation(command))
                    .isInstanceOf(NoSuchLineException.class);
        }

        @Test
        void 상행역_아이디에_해당하는_역이_없을시_예외() {
            // given
            given(loadStationPort.findById(upStationId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> sectionCommandService.addStation(command))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 하행역_아이디에_해당하는_역이_없을시_예외() {
            // given
            given(loadStationPort.findById(downStationId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> sectionCommandService.addStation(command))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 성공() {
            // when then
            AddStationToLineCommand command = new AddStationToLineCommand(lineId, upStationId, downStationId, 3);
            assertThatNoException().isThrownBy(() -> sectionCommandService.addStation(command));
        }
    }

    @Nested
    class 노선에서_역_제거시_ {

        private final long lineId = 1L;
        private final long stationId = 2L;
        private final RemoveStationFromLineCommand command = new RemoveStationFromLineCommand(lineId, stationId);

        @BeforeEach
        void setUp() {
            given(loadLinePort.findById(lineId))
                    .willReturn(Optional.of(new Line(lineId, "2호선", "GREEN", 0, List.of(이호선_역삼_삼성_3.SECTION))));
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.of(역삼역.STATION));
        }

        @Test
        void 라인아이디에_해당하는_라인이_없을시_예외() {
            // given
            given(loadLinePort.findById(lineId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> sectionCommandService.removeStation(command))
                    .isInstanceOf(NoSuchLineException.class);
        }

        @Test
        void 상행역_아이디에_해당하는_역이_없을시_예외() {
            // given
            given(loadStationPort.findById(stationId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> sectionCommandService.removeStation(command))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 성공() {
            // when then
            assertDoesNotThrow(() -> sectionCommandService.removeStation(command));
        }
    }
}
