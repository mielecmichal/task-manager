package pl.mielecmichal.taskmanager.queueing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.SimpleProcess;
import pl.mielecmichal.taskmanager.TaskManager;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

interface ConcurrencyTest extends TestedManagerProvider {

    @Test
    default void shouldHandleOperationsConcurrently() throws InterruptedException {
        //given
        TaskManager taskManager = provideTaskManager(50000);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        CountDownLatch latch = new CountDownLatch(5);

        executor.submit(processesGenerator(0, 10000, taskManager, latch));
        executor.submit(processesGenerator(10000, 20000, taskManager, latch));
        executor.submit(processesGenerator(20000, 30000, taskManager, latch));
        executor.submit(processesKiller(10000, 20000, taskManager, latch));
        executor.submit(processesKiller(20000, 30000, taskManager, latch));

        //when
        latch.await();
        Collection<Process> processes = taskManager.listProcesses(TaskManager.Sorting.BY_TIMESTAMP);

        Assertions.assertThat(processes).map(Process::pid)
                .allMatch(pid -> pid >= 0)
                .allMatch(pid -> pid < 10000)
                .containsExactlyInAnyOrderElementsOf(LongStream.range(0, 10000).boxed().collect(Collectors.toList()));
    }

    private Runnable processesGenerator(long from, long to, TaskManager manager, CountDownLatch latch) {
        return () -> {
            LongStream.range(from, to).forEach(i -> manager.addProcess(new SimpleProcess(i, Process.Priority.MEDIUM)));
            latch.countDown();
        };
    }

    private Runnable processesKiller(long from, long to, TaskManager manager, CountDownLatch latch) {
        return () -> {
            LongStream.range(0, 1000).forEach(value -> manager
                    .listProcesses(TaskManager.Sorting.BY_PID)
                    .stream()
                    .map(Process::pid)
                    .filter(pid -> pid >= from)
                    .filter(pid -> pid < to)
                    .forEach(manager::killProcess));
            latch.countDown();
        };
    }
}
