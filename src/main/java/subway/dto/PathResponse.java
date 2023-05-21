package subway.dto;

import java.util.List;

public class PathResponse {

    private List<LineAndSectionsResponse> lineAndSectionsResponse;
    private DistanceResponse distanceResponse;
    private FareResponse fareResponse;

    public PathResponse() {
    }

    public PathResponse(final List<LineAndSectionsResponse> lineAndSectionsResponses, final DistanceResponse distanceResponse, final FareResponse fareResponse) {
        this.lineAndSectionsResponse = lineAndSectionsResponses;
        this.distanceResponse = distanceResponse;
        this.fareResponse = fareResponse;
    }

    public List<LineAndSectionsResponse> getLineAndSectionsResponse() {
        return lineAndSectionsResponse;
    }

    public FareResponse getFareResponse() {
        return fareResponse;
    }

    public DistanceResponse getDistanceResponse() {
        return distanceResponse;
    }
}
