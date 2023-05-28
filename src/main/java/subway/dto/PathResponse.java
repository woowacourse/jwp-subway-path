package subway.dto;

import java.util.List;

public class PathResponse {

    private List<LineAndSectionsResponse> lineAndSectionsResponse;
    private Integer distance;
    private Integer fare;

    public PathResponse() {
    }

    public PathResponse(final List<LineAndSectionsResponse> lineAndSectionsResponses, final Integer distance, final Integer fare) {
        this.lineAndSectionsResponse = lineAndSectionsResponses;
        this.distance = distance;
        this.fare = fare;
    }

    public List<LineAndSectionsResponse> getLineAndSectionsResponse() {
        return lineAndSectionsResponse;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
