package subway.application.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.line.dto.LineRequest;
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.application.port.in.line.CreateLineUseCase;
import subway.application.port.in.line.DeleteLineUseCase;
import subway.domain.Line;

import java.util.Optional;

@Service
@Transactional
public class LineCommandService implements CreateLineUseCase, DeleteLineUseCase {

    private final LineJdbcAdapter lineJdbcAdapter;

    public LineCommandService(final LineJdbcAdapter lineJdbcAdapter) {
        this.lineJdbcAdapter = lineJdbcAdapter;
    }

    public Long createLine(final LineRequest lineRequest) {
        final Line createLine = new Line(lineRequest.getName());

        Optional<Line> line = lineJdbcAdapter.findByName(createLine);

        if (line.isPresent()) {
            throw new IllegalArgumentException("이미 저장되어있는 노선입니다.");
        }

        return lineJdbcAdapter.createLine(line.get());
    }
    public void deleteLine(final Long lineIdRequest) {
        lineJdbcAdapter.deleteById(lineIdRequest);
    }
}
