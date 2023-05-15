package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class Paths {
    private List<Path> paths;

    public Paths(final List<Path> paths) {
        this.paths = paths;
    }

    public Paths() {
        paths = new ArrayList<>();
    }

    public Paths addPath(final Path newPath) {
        validate(newPath);
        final List<Path> newPaths = new ArrayList<>(paths);

        if (isHeadOrTail(newPath)) {
            newPaths.add(newPath);
            return new Paths(newPaths);
        }

        paths.stream()
                .filter(path -> path.isUpStationEquals(newPath) || path.isDownStationEquals(newPath))
                .findAny()
                .ifPresent(path -> {
                    newPaths.remove(path);
                    newPaths.addAll(path.divideBy(newPath));
                });
        return new Paths(newPaths);
    }

    private void validate(final Path newPath) {
        if (paths.isEmpty()) {
            return;
        }

        validateExistedPath(newPath);
    }

    private void validateExistedPath(final Path newPath) {
        final Station up = newPath.getUp();
        final Station down = newPath.getDown();

        final long alreadyExistedStationsCount = paths.stream()
                .flatMap(path -> Stream.of(path.getUp(), path.getDown()))
                .distinct()
                .filter(station -> station.equals(up) || station.equals(down))
                .count();

        if (alreadyExistedStationsCount == 2) {
            throw new IllegalArgumentException("이미 존재하는 경로입니다.");
        }
        if (alreadyExistedStationsCount == 0) {
            throw new IllegalArgumentException("기존의 역과 이어져야 합니다.");
        }
    }

    private boolean isHeadOrTail(final Path newPath) {
        return paths.stream()
                .filter(path -> path.isUpStationEquals(newPath) || path.isDownStationEquals(newPath))
                .findAny()
                .isEmpty();
    }

    public Paths removePath(final Station station) {
        final List<Path> affectedPaths = findAffectedPaths(station);

        final List<Path> newPaths = new ArrayList<>(paths);
        newPaths.removeAll(affectedPaths);

        if (affectedPaths.size() == 1) {
            return new Paths(newPaths);
        }

        final Path path1 = affectedPaths.get(0);
        final Path path2 = affectedPaths.get(1);
        newPaths.add(path1.merge(path2));
        return new Paths(newPaths);
    }

    private List<Path> findAffectedPaths(final Station station) {
        return this.paths.stream()
                .filter(path -> path.contains(station))
                .collect(Collectors.toList());
    }

    public List<Path> getOrderedPaths() {
        if (paths.isEmpty()) {
            return Collections.emptyList();
        }

        final Path start = findStartPath();
        return Stream.iterate(start, findNextPath())
                .limit(paths.size())
                .collect(toUnmodifiableList());
    }

    private Path findStartPath() {
        for (Path path : paths) {
            if (notExistUpPath(path)) {
                return path;
            }
        }

        throw new IllegalStateException();
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
                .orElseThrow(IllegalStateException::new);
    }

    public List<Path> toList() {
        return new ArrayList<>(paths);
    }
}
