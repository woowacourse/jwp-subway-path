package subway.line.dto.response;

import java.util.List;

public class LinesResponseDto {

    private List<LineResponseDto> lines;

    private LinesResponseDto() {
    }

    public LinesResponseDto(List<LineResponseDto> lines) {
        this.lines = lines;
    }

    public List<LineResponseDto> getLines() {
        return lines;
    }
}
