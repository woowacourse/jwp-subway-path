package subway.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class Subway {
    private final List<Sections> sections;

    public Subway() {
        this.sections = new ArrayList<>();
    }

    public Station addStation(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Sections sections = findSectionsOf(line);
        return sections.addStation(upStation, downStation, distance);
    }

    public Sections findSectionsOf(final Line line) {
        return sections.stream().filter(element -> element.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 노선이 존재하지 않습니다."));
    }

    public List<Station> findStationsInOrderOf(final Line line) {
        final Sections sections = findSectionsOf(line);
        return sections.findAllStationsInOrder();
    }

    public Map<List<Station>, Integer> findAllSectionsInOrder(final Line line) {
        final Sections sections = findSectionsOf(line);
        return sections.findAllSectionsInOrder();
    }

    public int findDistanceBetween(final Line line, final Station upStation, final Station downStation) {
        final Sections sections = findSectionsOf(line);
        return sections.findDistanceBetween(upStation, downStation);
    }
}
