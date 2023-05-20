package subway.dto.request;

public class LineCreateRequest {
	private String name;

	public LineCreateRequest() {
	}

	public LineCreateRequest(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void validateName() {
		if (name.isBlank()) {
			throw new IllegalArgumentException("노선의 이름은 공백일 수 없습니다");
		}
		if (name.length() < 2 || name.length() > 10) {
			throw new IllegalArgumentException("노선의 이름은 최소 2글자 이상 최대 10글자 이하로 등록해주세요");
		}
	}
}
