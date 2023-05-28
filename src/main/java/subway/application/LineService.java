package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.Sections;
import subway.dto.LineSectionDto;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;
import subway.exception.custom.LineNotExistException;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse create(final LineRequest request) {
        final Line line = lineDao.insert(Line.withNullId(request.getName(), request.getColor(),
            request.getAdditionalFare()));
        return LineResponse.of(line);
    }

    public List<LineResponse> findAll() {
        final List<LineSectionDto> allLineWithSections = lineDao.findAllSections();
        return LineResponse.of(allLineWithSections);
    }

    public LineResponse findById(final Long id) {
        final Line line = lineDao.findById(id)
            .orElseThrow(() -> new LineNotExistException("노선이 존재하지 않습니다."));
        final List<Section> sectionsInLine = sectionDao.findAllByLineId(id);
        final List<StationResponse> stationResponses = Sections.of(sectionsInLine).getSortedStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return LineResponse.withStationResponses(line, stationResponses);
    }

    public void update(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(Line.of(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(),
            lineUpdateRequest.getAdditionalFare()));
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
