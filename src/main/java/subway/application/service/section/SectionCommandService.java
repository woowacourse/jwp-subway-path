package subway.application.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.section.dto.SectionCreateRequest;
import subway.adapter.in.web.section.dto.SectionDeleteRequest;
import subway.application.port.in.section.AttachStationUseCase;
import subway.application.port.in.section.DetachStationUseCase;
import subway.application.port.out.line.LineQueryPort;
import subway.application.port.out.section.SectionCommandPort;
import subway.application.port.out.section.SectionQueryPort;
import subway.application.port.out.station.StationQueryPort;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SectionCommandService implements AttachStationUseCase, DetachStationUseCase {

    private final LineQueryPort lineQueryPort;
    private final SectionCommandPort sectionCommandPort;
    private final SectionQueryPort sectionQueryPort;
    private final StationQueryPort stationQueryPort;

    public SectionCommandService(final LineQueryPort lineCommandPort, final SectionCommandPort sectionCommandPort, final SectionQueryPort sectionQueryPort, final StationQueryPort stationQueryPort) {
        this.lineQueryPort = lineCommandPort;
        this.sectionCommandPort = sectionCommandPort;
        this.sectionQueryPort = sectionQueryPort;
        this.stationQueryPort = stationQueryPort;
    }

    public void createSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        validateLineId(lineId);

        final Section section = createBy(lineId, sectionCreateRequest);
        Sections sections = new Sections(sectionQueryPort.findAllByLineId(lineId));

        sections.addSection(section);
        sectionCommandPort.saveSection(lineId, sections.getSections());
    }

    public void deleteStation(final Long lineId, final SectionDeleteRequest sectionDeleteRequest) {
        validateLineId(lineId);

        final Station station = stationQueryPort.findByName(new Station(sectionDeleteRequest.getStationName()))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));

        final List<Section> findSections = sectionQueryPort.findAllByLineId(lineId);
        if (findSections.isEmpty()) {
            throw new IllegalArgumentException("노선의 역이 없습니다.");
        }
        Sections sections = new Sections(findSections);
        sections.remove(station);

        sectionCommandPort.saveSection(lineId, sections.getSections());
    }

    private void validateLineId(final long lineId) {
        final Optional<Line> optionalLine = lineQueryPort.findLineById(lineId);
        if (optionalLine.isEmpty()) {
            throw new IllegalArgumentException("노선이 없습니다");
        }
    }

    private Section createBy(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        final Station upStation = stationQueryPort.findByName(new Station(sectionCreateRequest.getUpStationName()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        final Station downStation = stationQueryPort.findByName(new Station(sectionCreateRequest.getDownStationName()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        return new Section(
                lineId,
                upStation,
                downStation,
                sectionCreateRequest.getDistance()
        );
    }
}
