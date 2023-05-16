package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

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
                            result.addAll(originalPath.divideBy(path));
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
        final Station up = newPath.getUp();
        final Station down = newPath.getDown();

        return getStations().stream()
                .filter(station -> station.equals(up) || station.equals(down))
                .count();
    }

    private Optional<Path> findOverlappedOriginalPath(final Path newPath) {
        return paths.stream()
                .filter(path -> path.isUpStationEquals(newPath) || path.isDownStationEquals(newPath))
                .findAny();
    }

    public Paths removePath(final Station station) {
        final List<Path> result = new ArrayList<>(paths);

        final List<Path> affectedPaths = findAffectedPaths(station);
        result.removeAll(affectedPaths);

        if (isStationBetween(affectedPaths)) {
            mergeBothSides(result, affectedPaths);
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

    private void mergeBothSides(final List<Path> result, final List<Path> affectedPaths) {
        final Path previous = affectedPaths.get(0);
        final Path next = affectedPaths.get(1);

        result.add(previous.merge(next));
    }

    public List<Path> getOrdered() {
        if (paths.isEmpty()) {
            return Collections.emptyList();
        }

        final Path start = findStartPath();
        return Stream.iterate(start, findNextPath())
                .limit(paths.size())
                .collect(toUnmodifiableList());
    }

    private Path findStartPath() {
        return paths.stream()
                .filter(this::notExistUpPath)
                .findAny()
                .orElseThrow();
    }

    private boolean notExistUpPath(final Path path) {
        return paths.stream()
                .filter(path::isUpPath)
                .findAny()
                .isEmpty();
    }

    private UnaryOperator<Path> findNextPath() {
        return before -> paths.stream()
                .filter(before::isDownPath)
                .findAny()
                .orElseThrow();
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

    public int calculateCost(final FareStrategy fareStrategy) {
        return fareStrategy.calculate(getTotalDistance());
    }

    public List<Path> toList() {
        return new ArrayList<>(paths);
    }
}
