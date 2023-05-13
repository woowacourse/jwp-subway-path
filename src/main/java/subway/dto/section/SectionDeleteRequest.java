package subway.dto.section;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionDeleteRequest {

    @NotNull(message = "노선 번호를 입력해주세요.")
    private long lineNumber;

    @NotBlank(message = "삭제할 역을 입력해주세요.")
    private String station;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final long lineNumber, final String station) {
        this.lineNumber = lineNumber;
        this.station = station;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getStation() {
        return station;
    }
}
