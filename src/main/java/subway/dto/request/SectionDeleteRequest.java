package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionDeleteRequest {

	@NotNull(message = "노선 이름을 입력해주세요")
	private String lineName;

	@NotBlank(message = "삭제할 역을 입력해주세요")
	private String station;

	private SectionDeleteRequest() {
	}

	public SectionDeleteRequest(final String lineName, final String station) {
		this.lineName = lineName;
		this.station = station;
	}

	public String getLineName() {
		return lineName;
	}

	public String getStation() {
		return station;
	}
}
