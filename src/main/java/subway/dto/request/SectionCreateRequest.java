package subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionCreateRequest {

	@NotBlank(message = "노선 이름을 입력해주세요")
	private String lineName;

	@NotBlank(message = "상행역을 입력해주세요")
	private String upStation;

	@NotBlank(message = "하행역을 입력해주세요")
	private String downStation;

	@Min(value = 1, message = "구간의 최소 거리는 1 입니다")
	@NotNull(message = "거리를 입력해주세요")
	private long distance;

	private SectionCreateRequest() {
	}

	public SectionCreateRequest(final String lineName, final String upStation, final String downStation, final long distance) {
		this.lineName = lineName;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public String getLineName() {
		return lineName;
	}

	public String getUpStation() {
		return upStation;
	}

	public String getDownStation() {
		return downStation;
	}

	public long getDistance() {
		return distance;
	}
}
