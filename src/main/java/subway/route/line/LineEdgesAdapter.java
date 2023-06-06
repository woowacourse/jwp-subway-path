package subway.route.line;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import subway.line.application.LineQueryService;
import subway.line.application.dto.response.InterStationResponseDto;
import subway.line.application.dto.response.LineResponseDto;
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
        List<LineResponseDto> allLines = lineQueryService.findAllLines();
        List<InterStationEdge> edges = new ArrayList<>();
        for (LineResponseDto lineResponseDto : allLines) {
            addEdges(edges, lineResponseDto);
        }
        return edges;
    }

    private void addEdges(List<InterStationEdge> edges, LineResponseDto lineResponseDto) {
        List<InterStationResponseDto> interStationResponseDtos =
                lineResponseDto.getInterStations();
        for (InterStationResponseDto interStationResponseDto : interStationResponseDtos) {
            InterStationEdge edge = createEdge(interStationResponseDto, lineResponseDto.getId());
            edges.add(edge);
        }
    }

    private InterStationEdge createEdge(InterStationResponseDto responseDto, long lineId) {
        return new InterStationEdge(responseDto.getUpStationId(), responseDto.getDownStationId(),
                responseDto.getDistance(), lineId);
    }
}
