package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Sections;
import subway.entity.LineEntity;
import subway.exception.NotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line saveNewLine(Line line) {
        LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        //todo 1: 저장하기 전 노선의 이름과 색깔에 대한 중복검사 하기
        long lineId = lineDao.insert(lineEntity);
        return new Line(lineId, line.getName(), line.getColor(), new Sections(line.getSections()));
    }

    public Line findLineById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                new Sections(sectionDao.findSectionsByLineId(lineId)));
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                        new Sections(sectionDao.findSectionsByLineId(lineEntity.getId()))))
                .collect(Collectors.toList());
    }

    public void updateLineInfo(Line line) {
        //todo :수정하기 전 노선의 이름과 색깔에 대한 중복검사 하기
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor()));
    }

    public void delete(Long lineId) {
        //todo 3: 삭제하기 전 존재하는 라인인지 확인해보기 =>현재 서비스에서 해주고 있는데 수정해야 한다
        lineDao.deleteById(lineId);
    }

}
