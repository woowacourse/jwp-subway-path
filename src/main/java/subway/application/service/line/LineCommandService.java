package subway.application.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.line.dto.LineRequest;
import subway.application.port.in.line.CreateLineUseCase;
import subway.application.port.in.line.DeleteLineUseCase;
import subway.application.port.out.line.LineCommandHandler;
import subway.application.port.out.line.LineQueryHandler;
import subway.domain.Line;

import java.util.Optional;

@Service
@Transactional
public class LineCommandService implements CreateLineUseCase, DeleteLineUseCase {

    private final LineCommandHandler lineCommandHandler;
    private final LineQueryHandler lineQueryHandler;

    public LineCommandService(final LineCommandHandler lineCommandHandler, final LineQueryHandler lineQueryHandler) {
        this.lineCommandHandler = lineCommandHandler;
        this.lineQueryHandler = lineQueryHandler;
    }

    public Long createLine(final LineRequest lineRequest) {
        final Line createLine = new Line(lineRequest.getName(), lineRequest.getSurcharge());

        Optional<Line> line = lineQueryHandler.findByName(createLine);

        if (line.isPresent()) {
            throw new IllegalArgumentException("이미 저장되어있는 노선입니다.");
        }

        return lineCommandHandler.createLine(createLine);
    }
    public void deleteLine(final Long lineIdRequest) {
        lineCommandHandler.deleteById(lineIdRequest);
    }
}
