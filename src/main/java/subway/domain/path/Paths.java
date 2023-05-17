package subway.domain.path;

import subway.domain.Station;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static subway.domain.path.PathUtils.*;

public final class Paths {
    private final List<Path> paths;

    public Paths(final List<Path> paths) {
        this.paths = paths;
    }

    public Paths() {
        paths = new ArrayList<>();
    }

    public Paths addPath(final Path path) {
        validate(path);

        final List<Path> result = new ArrayList<>(paths);
        findOverlappedOriginalPath(path)
                .ifPresentOrElse(originalPath -> {
                            result.remove(originalPath);
                            result.addAll(divide(originalPath, path));
                        },
                        () -> result.add(path));

        return new Paths(result);
    }

    private void validate(final Path newPath) {
        if (paths.isEmpty()) {
            return;
        }

        final long countExisted = countStationsInOriginalPaths(newPath);
        if (countExisted == 2) {
            throw new IllegalArgumentException("이미 존재하는 경로입니다.");
        }
        if (countExisted == 0) {
            throw new IllegalArgumentException("기존의 역과 이어져야 합니다.");
        }
    }

    private long countStationsInOriginalPaths(final Path newPath) {
        return getStations().stream()
                .filter(newPath::contains)
                .count();
    }

    private Optional<Path> findOverlappedOriginalPath(final Path newPath) {
        return paths.stream()
                .filter(path -> isOverlapped(path, newPath))
                .findAny();
    }

    public Paths removePath(final Station station) {
        final List<Path> result = new ArrayList<>(paths);

        final List<Path> affectedPaths = findAffectedPaths(station);
        result.removeAll(affectedPaths);

        if (isStationBetween(affectedPaths)) {
            final Path merged = merge(affectedPaths.get(0), affectedPaths.get(1));
            result.add(merged);
        }

        return new Paths(result);
    }

    private List<Path> findAffectedPaths(final Station station) {
        return this.paths.stream()
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
                .flatMap(path -> Stream.of(path.getUp(), path.getDown()))
                .distinct()
                .collect(Collectors.toList());
    }

    public long getTotalDistance() {
        return paths.stream()
                .mapToLong(Path::getDistance)
                .sum();
    }

    public List<Path> toList() {
        return new ArrayList<>(paths);
    }
}