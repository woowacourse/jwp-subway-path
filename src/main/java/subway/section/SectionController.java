package subway.section;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.LineService;
import subway.line.persistence.LineEntity;
import subway.section.dto.SectionDeleteDto;
import subway.section.persistence.SectionEntity;
import subway.station.StationService;
import subway.station.persistence.StationEntity;

@RestController
public class SectionController {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionService sectionService;

    public SectionController(final StationService stationService, final LineService lineService,
        final SectionService sectionService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping("/section")
    public void create(@RequestBody SectionCreateDto sectionCreateDto) {
        final LineEntity lineEntity = lineService.findById(sectionCreateDto.getLineId());
        final StationEntity upStation = stationService.findByName(sectionCreateDto.getUp());
        final StationEntity downStation = stationService.findByName(sectionCreateDto.getDown());

        final SectionEntity sectionEntity = new SectionEntity(lineEntity.getId(), upStation.getId(),
            downStation.getId(), sectionCreateDto.getDistance());

        sectionService.addSection(sectionEntity, upStation.getName(), downStation.getName());
    }

    @DeleteMapping("/section")
    public void delete(@RequestBody final SectionDeleteDto sectionDeleteDto) {
        sectionService.removeStationBy(sectionDeleteDto);
    }
}
