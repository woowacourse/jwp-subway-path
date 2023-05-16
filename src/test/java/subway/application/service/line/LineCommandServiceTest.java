package subway.application.service.line;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.application.port.in.line.dto.command.UpdateLineInfoCommand;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.line.PersistLinePort;
import subway.common.exception.NoSuchLineException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineCommandServiceTest {

    private LoadLinePort loadLinePort;
    private PersistLinePort persistLinePort;
    private LineCommandService lineCommandService;

    @BeforeEach
    void setUp() {
        loadLinePort = mock(LoadLinePort.class);
        persistLinePort = mock(PersistLinePort.class);
        lineCommandService = new LineCommandService(loadLinePort, persistLinePort);
    }

    @Nested
    class 노선_정보_갱신시_ {

        private final long lineId = 1L;
        private final UpdateLineInfoCommand command = new UpdateLineInfoCommand(lineId, "2호선", "GREEN");

        @Test
        void 아이디에_해당하는_노선이_존재하지_않으면_예외() {
            // given
            given(loadLinePort.checkExistById(lineId)).willReturn(false);

            // when then
            assertThatThrownBy(() -> lineCommandService.updateLineInfo(command))
                    .isInstanceOf(NoSuchLineException.class);
        }

        @Test
        void 성공() {
            // given
            given(loadLinePort.checkExistById(lineId)).willReturn(true);

            // when then
            assertThatNoException().isThrownBy(() -> lineCommandService.updateLineInfo(command));
        }
    }

    @Nested
    class 노선_삭제시_ {

        private final long lineId = 1L;

        @Test
        void 아이디에_해당하는_노선이_존재하지_않으면_예외() {
            // given
            long lineId = 1L;
            given(loadLinePort.checkExistById(lineId)).willReturn(false);

            // when then
            assertThatThrownBy(() -> lineCommandService.deleteLine(lineId))
                    .isInstanceOf(NoSuchLineException.class);
        }

        @Test
        void 성공() {
            // given
            given(loadLinePort.checkExistById(lineId)).willReturn(true);

            // when then
            assertThatNoException().isThrownBy(() -> lineCommandService.deleteLine(lineId));
        }
    }
}
