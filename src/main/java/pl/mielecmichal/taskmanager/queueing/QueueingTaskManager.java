package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.TaskManager;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class QueueingTaskManager implements TaskManager {

    private final int size;
    private final Queue<Task> queue;
    private final OverflowStrategy overflowStrategy;

    public QueueingTaskManager(int size, OverflowStrategy overflowStrategy) {
        this.size = size;
        this.overflowStrategy = overflowStrategy;
        this.queue = new PriorityQueue<>(size, overflowStrategy.queueComparator);
    }

    @Override
    public void addProcess(Process process) {
        checkProcess(process);

        Task newTask = new Task(process);
        if (isOverflowed()) {
            overflowStrategy.overflowHandler.accept(newTask, queue);
            if (isOverflowed()) {
                return;
            }
        }
        queue.add(newTask);
    }

    @Override
    public Collection<Process> listProcesses(Sorting sorting) {
        return queue.stream()
                .sorted(TaskComparator.of(sorting))
                .map(Task::process)
                .collect(Collectors.toList());
    }

    @Override
    public void killProcess(long pid) {
        killIf(task -> task.process().pid() == pid);
    }

    @Override
    public void killGroup(Process.Priority priority) {
        killIf(task -> task.process().priority() == priority);
    }

    @Override
    public void killAll() {
        killIf(task -> true);
    }

    private void killIf(Predicate<Task> predicate) {
        queue.stream()
                .filter(predicate)
                .forEach(Task::kill);
        queue.removeIf(predicate);
    }

    private boolean isOverflowed() {
        return queue.size() >= size;
    }

    private void checkProcess(Process process) {
        if (process == null) {
            throw new IllegalArgumentException(
                    "Process cannot be null"
            );
        }

        long pid = process.pid();
        boolean duplicated = queue.stream().anyMatch(task -> task.process().pid() == pid);
        if (duplicated) {
            throw new IllegalArgumentException(
                    "Process with PID=%d is already present and cannot be added second time".formatted(pid)
            );
        }
    }
}
