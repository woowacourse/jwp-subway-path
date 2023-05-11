package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.LineCreateDto;
import subway.dto.LineDto;

@Service
public class LineCreateServiceImpl implements LineCreateService {

    private final SubwayGraphs subwayGraphs;

    private final LineDao lineDao;

    public LineCreateServiceImpl(final SubwayGraphs subwayGraphs, final LineDao lineDao) {
        this.subwayGraphs = subwayGraphs;
        this.lineDao = lineDao;
    }

    @Override
    public void createLine(final LineCreateDto lineCreateDto) {
        final Line line = new Line(lineCreateDto.getLineName());
        final Station upLineStation = new Station(lineCreateDto.getUpLineStationName());
        final Station downLineStation = new Station(lineCreateDto.getDownLineStationName());
        final int distance = (int) lineCreateDto.getDistance();

        final LineDto lineDto = subwayGraphs.createLine(line, upLineStation, downLineStation, distance);

        // TODO: Station entity 저장 -> station id 가져오기

        // TODO: Line entity 생성 -> dao 호출
    }
}
