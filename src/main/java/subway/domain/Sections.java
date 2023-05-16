package subway.domain;

import subway.application.exception.SubwayInternalServerException;
import subway.application.exception.SubwayServiceException;

import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        if (sections.size() > 2) {
            throw new SubwayInternalServerException("구간의 크기가 2를 초과합니다.");
        }
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Long getLeftStationId() {
        if (sections.size() == 1) {
            return sections.get(0).getLeftId();
        }
        if (sections.get(0).getLeft().equals(sections.get(1).getRight())) {
            return sections.get(1).getLeftId();
        }
        return sections.get(0).getLeftId();
    }

    public Long getRightStationId() {
        if (sections.size() == 1) {
            return sections.get(0).getRightId();
        }
        if (sections.get(0).getRight().equals(sections.get(1).getLeft())) {
            return sections.get(1).getRightId();
        }
        return sections.get(0).getRightId();
    }

    public boolean isPresent() {
        return !sections.isEmpty();
    }

    public Section getFirstSection() {
        if (isPresent()) {
            return sections.get(0);
        }
        throw new SubwayInternalServerException("구간이 존재하지 않습니다.");
    }
}
