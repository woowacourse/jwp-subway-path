package subway.application.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.section.dto.SectionCreateRequest;
import subway.adapter.in.web.section.dto.SectionDeleteRequest;
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.adapter.out.persistence.repository.SectionJdbcAdapter;
import subway.adapter.out.persistence.repository.StationJdbcAdapter;
import subway.application.port.in.section.AttachStationUseCase;
import subway.application.port.in.section.DetachStationUseCase;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SectionCommandService implements AttachStationUseCase, DetachStationUseCase {

    private final LineJdbcAdapter lineJdbcAdapter;

    private final StationJdbcAdapter stationJdbcAdapter;
    private final SectionJdbcAdapter sectionJdbcAdapter;

    public SectionCommandService(
            final LineJdbcAdapter lineJdbcAdapter,
            final StationJdbcAdapter stationJdbcAdapter,
            final SectionJdbcAdapter sectionJdbcAdapter
    ) {
        this.lineJdbcAdapter = lineJdbcAdapter;
        this.stationJdbcAdapter = stationJdbcAdapter;
        this.sectionJdbcAdapter = sectionJdbcAdapter;
    }

    public void createSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        validateLineId(lineId);

        final Section section = createBy(lineId, sectionCreateRequest);
        Sections sections = new Sections(sectionJdbcAdapter.findAllByLineId(lineId));

        sections.addSection(section);
        sectionJdbcAdapter.saveSection(lineId, sections.getSections());
    }

    public void deleteStation(final Long lineId, final SectionDeleteRequest sectionDeleteRequest) {
        validateLineId(lineId);

        final Station station = stationJdbcAdapter.findByName(new Station(sectionDeleteRequest.getStationName()))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));

        final List<Section> findSections = sectionJdbcAdapter.findAllByLineId(lineId);
        if (findSections.isEmpty()) {
            throw new IllegalArgumentException("노선의 역이 없습니다.");
        }
        Sections sections = new Sections(findSections);
        sections.remove(station);

        sectionJdbcAdapter.saveSection(lineId, sections.getSections());
    }

    private void validateLineId(final long lineId) {
        final Optional<Line> optionalLine = lineJdbcAdapter.findById(lineId);
        if (optionalLine.isEmpty()) {
            throw new IllegalArgumentException("노선이 없습니다");
        }
    }

    private Section createBy(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        final Station upStation = stationJdbcAdapter.findByName(new Station(sectionCreateRequest.getUpStationName()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        final Station downStation = stationJdbcAdapter.findByName(new Station(sectionCreateRequest.getDownStationName()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        return new Section(
                lineId,
                upStation,
                downStation,
                sectionCreateRequest.getDistance()
        );
    }
}
