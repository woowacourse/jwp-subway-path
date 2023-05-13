package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.domain.SubwayLine;
import subway.line.dto.LineResponseDto;
import subway.line.persistence.LineEntity;
import subway.section.SectionService;
import subway.station.dto.StationResponseDto;
import subway.station.persistence.StationEntity;
import subway.station.util.StationEntityConverter;

@RestController
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponseDto>> getLines() {
        final List<LineEntity> lines = lineService.findAll();
        final List<LineResponseDto> lineResponseDtos = lines.stream()
            .map((line) -> new LineResponseDto(line.getId(), line.getName(),
                getStationResponseDtosByLineId(line.getId())))
            .collect(Collectors.toList());

        return ResponseEntity.ok(lineResponseDtos);
    }


    private List<StationResponseDto> getStationResponseDtosByLineId(final Long lineId) {
        final List<StationEntity> stationEntities = sectionService.findSortedStationEntityByLineId(lineId);
        return StationEntityConverter.convertToStationResponseDto(stationEntities);
    }

    @GetMapping("/line/{lineId}")
    public ResponseEntity<List<StationResponseDto>> getStation(@PathVariable(name = "lineId") final Long lineId) {
        final List<StationResponseDto> responseDtos = getStationResponseDtosByLineId(lineId);
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("/line")
    public void create(@RequestBody LineCreateDto lineCreateDto) {
        lineService.create(SubwayLine.register(lineCreateDto.getName()));
    }
}
