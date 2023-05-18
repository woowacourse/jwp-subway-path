package subway.ui.line.dto.in;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinesResponse {

    private List<LineResponse> lines;

    private LinesResponse() {
        this(null);
    }
}
