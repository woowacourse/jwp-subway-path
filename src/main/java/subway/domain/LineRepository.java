package subway.domain;

import java.util.List;

public interface LineRepository {

    List<Line> findLinesWithSort();
    List<Section> findSectionsWithSort();
    List<Section> findSectionsByLineIdWithSort(Long lineId);
}
