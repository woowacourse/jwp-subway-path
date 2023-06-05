package subway.route.in.find;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import subway.line.application.port.in.InterStationResponseDto;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.findall.LineFindAllUseCase;
import subway.route.application.port.in.find.RouteAllEdgesUseCase;
import subway.route.domain.Edges;
import subway.route.domain.InterStationEdge;

@Component
public class LineEdgesAdapter implements RouteAllEdgesUseCase {

    private final LineFindAllUseCase lineFindAllUseCase;

    public LineEdgesAdapter(LineFindAllUseCase lineFindAllUseCase) {
        this.lineFindAllUseCase = lineFindAllUseCase;
    }

    @Override
    public Edges findAllEdges() {
        List<LineResponseDto> allLines = lineFindAllUseCase.findAllLines();
        List<InterStationEdge> edges = new ArrayList<>();
        for (LineResponseDto lineResponseDto : allLines) {
            addEdges(edges, lineResponseDto);
        }
        return new Edges(edges);
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
