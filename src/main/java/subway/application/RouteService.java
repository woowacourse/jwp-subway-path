package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.ShortcutFinder;
import subway.domain.vo.Shortcut;
import subway.domain.vo.Station;
import subway.dto.RouteResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private final SectionDao sectionDao;

    public RouteService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public RouteResponse findShortcut(final long departure, final long arrival) {
        final ShortcutFinder shortcutFinder = new ShortcutFinder(sectionDao.findAllSections());
        final Shortcut shortcut = shortcutFinder.findShortcut(departure, arrival);
        final List<String> path = shortcut.getPath()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        final int fee = shortcut.getFee();
        return new RouteResponse(path, fee);
    }

}
