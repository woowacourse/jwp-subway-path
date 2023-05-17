package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long save(final LineRequest lineRequest) {
        Subway subway = new Subway(lineRepository.findAll());
        Line line = new Line(new LineName(lineRequest.getName()), new LineColor(lineRequest.getColor()), Sections.create());
        Line savedLine = lineRepository.save(line);
        // TODO: 5/17/23 아무 일도 없었다.. 느낌..
        Subway updateSubway = subway.addLine(line);
        return savedLine.getId();
    }

    public List<LineResponse> findAllLines() {
        final List<Line> lines = lineRepository.findAll();

        List<LineResponse> lineResponses = new ArrayList<>();
        for (Line line : lines) {
            List<Station> stations = line.findAllStation();
            lineResponses.add(LineResponse.of(line, stations));
        }
        return lineResponses;
    }

    public LineResponse findStations(final Long lineId) {
        final Line line = lineRepository.findById(lineId);
        List<Station> stations = line.findAllStation();
        return LineResponse.of(line, stations);
    }

    @Transactional
    public void delete(final Long lineId) {
        Subway subway = new Subway(lineRepository.findAll());
        Subway updateSubway = subway.deleteLine(lineId);
        // TODO: 5/17/23 이렇게 직접 지우는게 나을까?
        //sectionsRepsitory.deleteByLineId(lineId);
        //lineRepository.delete(lineId);
        lineRepository.updateBySubway(subway, updateSubway);
    }
}
