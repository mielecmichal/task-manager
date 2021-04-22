package pl.mielecmichal.taskmanager.queueing;

import java.util.Comparator;
import java.util.Queue;
import java.util.function.BiConsumer;

enum OverflowStrategy {

    FIXED((task, queue) -> {}, TaskComparator.BY_PRIORITY),
    FIFO(OverflowStrategy::fifoStrategy, TaskComparator.BY_TIMESTAMP),
    PRIORITY(OverflowStrategy::prioritizedStrategy, TaskComparator.BY_PRIORITY.thenComparing(TaskComparator.BY_TIMESTAMP));

    public final BiConsumer<Task, Queue<Task>> overflowHandler;
    public final Comparator<Task> queueComparator;

    OverflowStrategy(BiConsumer<Task, Queue<Task>> overflowHandler, Comparator<Task> queueComparator) {
        this.overflowHandler = overflowHandler;
        this.queueComparator = queueComparator;
    }

    private static void fifoStrategy(Task newTask, Queue<Task> queue) {
        killNext(queue);
    }

    private static void prioritizedStrategy(Task newTask, Queue<Task> queue) {
        Task lowest = queue.peek();
        int nextProcessPriority = lowest.process().priority().value();
        int newProcessPriority = newTask.process().priority().value();
        if (nextProcessPriority <= newProcessPriority) {
            killNext(queue);
        }
    }

    private static void killNext(Queue<Task> queue){
        Task task = queue.poll();
        task.kill();
    }
}
