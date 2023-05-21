package subway.domain.path;

import subway.domain.Station;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class Paths {
    private static final int ALREADY_EXIST_PATH_SIZE = 2;

    private final List<Path> paths;

    public Paths(final List<Path> paths) {
        this.paths = paths;
        validate(paths);
    }

    private void validate(final List<Path> paths) {
        try {
            if (getOrdered().size() != paths.size()) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("연결되지 않은 경로들입니다.");
        }
    }

    public Paths() {
        this(new ArrayList<>());
    }

    public Paths addPath(final Path path) {
        validatePathInsertionAvailable(path.getStations());

        final List<Path> result = new ArrayList<>(paths);
        findOverlappedOriginalPath(path)
                .ifPresentOrElse(originalPath -> {
                            result.remove(originalPath);
                            result.addAll(originalPath.divide(path));
                        },
                        () -> result.add(path));

        return new Paths(result);
    }

    private void validatePathInsertionAvailable(final List<Station> pathStations) {
        if (pathStations.isEmpty() || paths.isEmpty()) {
            return;
        }

        pathStations.retainAll(getStations());
        if (pathStations.size() == ALREADY_EXIST_PATH_SIZE) {
            throw new IllegalArgumentException("이미 존재하는 경로입니다.");
        }
        if (pathStations.isEmpty()) {
            throw new IllegalArgumentException("기존의 역과 이어져야 합니다.");
        }
    }

    private Optional<Path> findOverlappedOriginalPath(final Path newPath) {
        return paths.stream()
                .filter(newPath::isOverlapped)
                .findAny();
    }

    public Paths removePath(final Station station) {
        final List<Path> result = new ArrayList<>(paths);

        final List<Path> affectedPaths = findAffectedPaths(station);
        result.removeAll(affectedPaths);

        if (isStationBetween(affectedPaths)) {
            final Path merged = affectedPaths.get(0).merge(affectedPaths.get(1));
            result.add(merged);
        }

        return new Paths(result);
    }

    private List<Path> findAffectedPaths(final Station station) {
        return paths.stream()
                .filter(path -> path.contains(station))
                .collect(Collectors.toList());
    }

    private boolean isStationBetween(final List<Path> affectedPaths) {
        return affectedPaths.size() == 2;
    }

    public List<Path> getOrdered() {
        if (paths.isEmpty()) {
            return Collections.emptyList();
        }

        final Map<Station, Path> pathsByUpStation = new HashMap<>();
        paths.forEach(path -> pathsByUpStation.put(path.getUp(), path));

        return Stream.iterate(getStartPath(), up -> pathsByUpStation.get(up.getDown()))
                .limit(paths.size())
                .collect(toUnmodifiableList());
    }

    private Path getStartPath() {
        final Station startStation = getStations().stream()
                .filter(this::isNotDownStationInAnyPath)
                .findAny().orElseThrow();

        return paths.stream()
                .filter(path -> path.isUpStation(startStation))
                .findAny().orElseThrow();
    }

    private boolean isNotDownStationInAnyPath(final Station station) {
        return paths.stream()
                .filter(path -> path.isDownStation(station))
                .findAny()
                .isEmpty();
    }

    public List<Station> getStations() {
        return paths.stream()
                .flatMap(path -> path.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Path> toList() {
        return new ArrayList<>(paths);
    }
}
