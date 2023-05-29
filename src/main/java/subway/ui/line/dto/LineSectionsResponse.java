package subway.ui.line.dto;

import java.util.List;

import subway.ui.section.dto.SectionResponse;

public class LineSectionsResponse {

	private LineResponse lineResponse;
	private List<SectionResponse> sectionResponses;

	public LineSectionsResponse(final LineResponse lineResponse, final List<SectionResponse> sectionResponses) {
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
