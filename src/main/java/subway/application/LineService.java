package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.LineRepository;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;

@Service
public class LineService {

    private final StationDao stationDao;

    private final LineRepository lineRepository;

    public LineService(StationDao stationDao, LineRepository lineRepository) {
        this.stationDao = stationDao;
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = new Line(request.getName(), request.getColor());
        Line saved = lineRepository.save(line);
        return LineResponse.of(saved);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line found = lineRepository.findBy(id);
        return LineResponse.of(found);
    }

    /* 학습용 주석입니다 :)
    DAO를 직접 사용해 DB에 업데이트하기 vs 도메인에 setter 넣기
    후자를 선택했다.
    DAO를 사용한다면 캡슐화를 지킬 순 있지만 서비스 레이어에서 저장 방법을 신경써야 한다.
    관심사가 섞여서 복잡도가 올라가고, 유지보수성과 유연성이 떨어진다. 이는 Repository를 만든 이유와 정반대된다.
    더불어 사용자가 정말 이름을 바꾸길 원한다면, 못 바꿀 이유는 또 뭔가?

    이름은 서비스 영역에서 마음대로 바꿔도 되는가?
    이름이 중복되면 안된다고 가정해보자.
    겹치는 이름이 있다면, 그건 이미 저장된 이름들이다.
    따라서 저장 영역에서 그런 이름이 가능한지 검증이 있으면 되지 않을까?
    그럼 관심사가 분리된 구조를 지키면서도 안전하게 요청을 처리할 수 있다.
    */
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findBy(id);

        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());

        lineRepository.save(line);
    }

    public void deleteLineById(Long id) {
        Line line = lineRepository.findBy(id);
        lineRepository.delete(line);
    }

    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findBy(lineId);
        Station upper = stationDao.findById(sectionRequest.getUpperStationId());
        Station lower = stationDao.findById(sectionRequest.getLowerStationId());

        Distance distance = new Distance(sectionRequest.getDistance());
        Section section = new Section(upper, lower, distance.getValue());
        line.add(section);

        lineRepository.save(line);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = lineRepository.findBy(lineId);
        Station station = stationDao.findById(stationId);

        line.remove(station);

        lineRepository.save(line);
    }
}
