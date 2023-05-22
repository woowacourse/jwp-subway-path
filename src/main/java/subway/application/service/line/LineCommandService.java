package subway.application.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.line.CreateLineUseCase;
import subway.application.port.in.line.DeleteLineUseCase;
import subway.application.port.in.line.UpdateLineInfoUseCase;
import subway.application.port.in.line.dto.command.CreateLineCommand;
import subway.application.port.in.line.dto.command.UpdateLineInfoCommand;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.line.PersistLinePort;
import subway.common.exception.NoSuchLineException;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.LineInfo;

@Service
@Transactional
public class LineCommandService implements CreateLineUseCase, UpdateLineInfoUseCase, DeleteLineUseCase {

    private final LoadLinePort loadLinePort;
    private final PersistLinePort persistLinePort;

    public LineCommandService(final LoadLinePort loadLinePort, final PersistLinePort persistLinePort) {
        this.loadLinePort = loadLinePort;
        this.persistLinePort = persistLinePort;
    }

    @Override
    public long createLine(final CreateLineCommand command) {
        if (loadLinePort.checkExistByName(command.getName())) {
            throw new SubwayIllegalArgumentException("기존 노선과 중복된 이름입니다.");
        }
        return persistLinePort.create(new LineInfo(command.getName(), command.getColor(), command.getSurcharge()));
    }

    @Override
    public void updateLineInfo(final UpdateLineInfoCommand command) {
        validateLineId(command.getLineId());

        persistLinePort.updateInfo(command.getLineId(),
                new LineInfo(command.getName(), command.getColor(), command.getSurcharge()));
    }

    @Override
    public void deleteLine(final long lineId) {
        validateLineId(lineId);

        persistLinePort.deleteSectionsByLineId(lineId);

        persistLinePort.deleteById(lineId);
    }

    private void validateLineId(final long lineId) {
        if (!loadLinePort.checkExistById(lineId)) {
            throw new NoSuchLineException();
        }
    }
}
