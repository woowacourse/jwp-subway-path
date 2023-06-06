package subway.route.line;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import subway.line.application.LineQueryService;
import subway.line.dto.response.InterStationResponse;
import subway.line.dto.response.LineResponse;
import subway.route.domain.InterStationEdge;
import subway.route.domain.RouteAllEdgesUseCase;

@Component
public class LineEdgesAdapter implements RouteAllEdgesUseCase {

    private final LineQueryService lineQueryService;

    public LineEdgesAdapter(LineQueryService lineQueryService) {
        this.lineQueryService = lineQueryService;
    }

    @Override
    public List<InterStationEdge> findAllEdges() {
        List<LineResponse> allLines = lineQueryService.findAllLines();
        List<InterStationEdge> edges = new ArrayList<>();
        for (LineResponse lineResponse : allLines) {
            addEdges(edges, lineResponse);
        }
        return edges;
    }

    private void addEdges(List<InterStationEdge> edges, LineResponse lineResponse) {
        List<InterStationResponse> interStationResponses =
                lineResponse.getInterStations();
        for (InterStationResponse interStationResponse : interStationResponses) {
            InterStationEdge edge = createEdge(interStationResponse, lineResponse.getId());
            edges.add(edge);
        }
    }

    private InterStationEdge createEdge(InterStationResponse responseDto, long lineId) {
        return new InterStationEdge(responseDto.getUpStationId(), responseDto.getDownStationId(),
                responseDto.getDistance(), lineId);
    }
}
