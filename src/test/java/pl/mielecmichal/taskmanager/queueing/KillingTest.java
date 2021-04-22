package pl.mielecmichal.taskmanager.queueing;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.TaskManager;
import pl.mielecmichal.taskmanager.TaskManager.Sorting;

import java.util.List;
import java.util.function.Consumer;

import static pl.mielecmichal.taskmanager.queueing.Prototypes.*;

interface KillingTest extends TestedManagerProvider {

    @ParameterizedTest
    @EnumSource(KillingScenario.class)
    default void shouldKill(KillingScenario scenario) {
        //given
        TaskManager taskManager = provideTaskManager(10);
        scenario.givenProcesses.forEach(taskManager::addProcess);

        //when
        scenario.killer.accept(taskManager);

        //then
        Assertions.assertThat(taskManager.listProcesses(Sorting.BY_TIMESTAMP))
                .containsExactlyElementsOf(scenario.expectedProcesses);
    }

    enum KillingScenario {
        SHOULD_KILL_ALL(
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_1, MEDIUM_PROCESS_2),
                TaskManager::killAll,
                List.of()
        ),
        SHOULD_KILL_ALL_WHEN_EMPTY(
                List.of(),
                TaskManager::killAll,
                List.of()
        ),
        SHOULD_KILL_GROUP(
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_1, MEDIUM_PROCESS_2, LOW_PROCESS_5),
                taskManager -> taskManager.killGroup(Process.Priority.MEDIUM),
                List.of(HIGH_PROCESS_4, LOW_PROCESS_5)
        ),
        SHOULD_KILL_GROUP_WHEN_EMPTY(
                List.of(),
                taskManager -> taskManager.killGroup(Process.Priority.MEDIUM),
                List.of()
        ),
        SHOULD_KILL_SINGLE_PROCESS(
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_1, MEDIUM_PROCESS_2, LOW_PROCESS_5),
                taskManager -> taskManager.killProcess(2),
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_1, LOW_PROCESS_5)
        ),
        SHOULD_KILL_SINGLE_PROCESS_WHEN_EMPTY(
                List.of(),
                taskManager -> taskManager.killProcess(2),
                List.of()
        ),
        SHOULD_IGNORE_NOT_EXISTING_PID(
                List.of(MEDIUM_PROCESS_1, LOW_PROCESS_5),
                taskManager -> taskManager.killProcess(2),
                List.of(MEDIUM_PROCESS_1, LOW_PROCESS_5)
        );

        private final List<Process> givenProcesses;
        private final Consumer<TaskManager> killer;
        private final List<Process> expectedProcesses;

        KillingScenario(List<Process> givenProcesses,
                        Consumer<TaskManager> killer,
                        List<Process> expectedProcesses) {
            this.givenProcesses = givenProcesses;
            this.killer = killer;
            this.expectedProcesses = expectedProcesses;
        }
    }
}