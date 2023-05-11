package subway.ui.dto;

import java.util.List;

public class LineStationResponse {

	private LineResponse lineResponse;
	private List<SectionResponse> sectionResponses;

	public LineStationResponse(final LineResponse lineResponse, final List<SectionResponse> sectionResponses) {
		this.lineResponse = lineResponse;
		this.sectionResponses = sectionResponses;
	}

	public LineResponse getLineResponse() {
		return lineResponse;
	}

	public List<SectionResponse> getSectionResponses() {
		return sectionResponses;
	}
}
