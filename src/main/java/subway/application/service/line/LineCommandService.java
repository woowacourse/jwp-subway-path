package subway.application.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.line.dto.LineRequest;
import subway.application.port.in.line.CreateLineUseCase;
import subway.application.port.in.line.DeleteLineUseCase;
import subway.application.port.out.line.LineCommandPort;
import subway.application.port.out.line.LineQueryPort;
import subway.domain.Line;

import java.util.Optional;

@Service
@Transactional
public class LineCommandService implements CreateLineUseCase, DeleteLineUseCase {

    private final LineCommandPort lineCommandPort;
    private final LineQueryPort lineQueryPort;

    public LineCommandService(final LineCommandPort lineCommandPort, final LineQueryPort lineQueryPort) {
        this.lineCommandPort = lineCommandPort;
        this.lineQueryPort = lineQueryPort;
    }

    public Long createLine(final LineRequest lineRequest) {
        final Line createLine = new Line(lineRequest.getName(), lineRequest.getSurcharge());

        Optional<Line> line = lineQueryPort.findByName(createLine);

        if (line.isPresent()) {
            throw new IllegalArgumentException("이미 저장되어있는 노선입니다.");
        }

        return lineCommandPort.createLine(createLine);
    }
    public void deleteLine(final Long lineIdRequest) {
        lineCommandPort.deleteById(lineIdRequest);
    }
}
