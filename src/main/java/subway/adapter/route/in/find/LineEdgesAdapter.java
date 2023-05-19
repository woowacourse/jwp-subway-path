package subway.adapter.route.in.find;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import subway.application.line.port.in.InterStationResponseDto;
import subway.application.line.port.in.LineFindAllUseCase;
import subway.application.line.port.in.LineResponseDto;
import subway.application.route.port.in.find.RouteAllEdgesUseCase;
import subway.domain.route.Edges;
import subway.domain.route.InterStationEdge;

@Component
public class LineEdgesAdapter implements RouteAllEdgesUseCase {

    private final LineFindAllUseCase lineFindAllUseCase;

    public LineEdgesAdapter(final LineFindAllUseCase lineFindAllUseCase) {
        this.lineFindAllUseCase = lineFindAllUseCase;
    }

    @Override
    public Edges findAllEdges() {
        final List<LineResponseDto> allLines = lineFindAllUseCase.findAllLines();
        final List<InterStationEdge> edges = new ArrayList<>();
        for (final LineResponseDto lineResponseDto : allLines) {
            addEdges(edges, lineResponseDto);
        }
        return new Edges(edges);
    }

    private void addEdges(final List<InterStationEdge> edges, final LineResponseDto lineResponseDto) {
        final List<InterStationResponseDto> interStationResponseDtos =
                lineResponseDto.getInterStations();
        for (final InterStationResponseDto interStationResponseDto : interStationResponseDtos) {
            final InterStationEdge edge = createEdge(interStationResponseDto, lineResponseDto.getId());
            edges.add(edge);
        }
    }

    private InterStationEdge createEdge(final InterStationResponseDto responseDto, final long lineId) {
        return new InterStationEdge(responseDto.getUpStationId(), responseDto.getDownStationId(),
                responseDto.getDistance(), lineId);
    }
}
