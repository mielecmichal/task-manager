package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.TaskManager.Sorting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

enum TaskComparator implements Comparator<Task> {

    BY_PID(Comparator.comparing(task -> task.process().pid())),
    BY_PRIORITY(Comparator.comparing(Task::priorityValue)),
    BY_TIMESTAMP(Comparator.comparing(Task::monotonicTimestamp));

    private static final Map<String, TaskComparator> AVAILABLE_COMPARATORS = Arrays.stream(values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    TaskComparator(Comparator<Task> comparator) {
        this.comparator = comparator;
    }

    public final Comparator<Task> comparator;

    @Override
    public int compare(Task o1, Task o2) {
        return comparator.compare(o1, o2);
    }

    @Override
    public Comparator<Task> thenComparing(Comparator<? super Task> other) {
        return comparator.thenComparing(other);
    }

    public static TaskComparator of(Sorting sorting) {
        return Optional.ofNullable(AVAILABLE_COMPARATORS.get(sorting.name()))
                .orElseThrow(() -> new IllegalArgumentException("Comparator for %s not available".formatted(sorting)));
    }
}
