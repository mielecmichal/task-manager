package pl.mielecmichal.taskmanager.queueing;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.TaskManager;
import pl.mielecmichal.taskmanager.TaskManager.Sorting;

import java.util.List;

import static pl.mielecmichal.taskmanager.queueing.Prototypes.*;


interface AddingFixedTest extends TestedManagerProvider {

    @ParameterizedTest
    @EnumSource(AddingScenario.class)
    default void shouldAddProcesses(AddingScenario scenario) {
        //given
        TaskManager taskManager = provideTaskManager(scenario.givenSize);
        List<Process> addedProcesses = scenario.givenProcesses;

        //when
        addedProcesses.forEach(taskManager::addProcess);

        //then
        List<Process> expectedProcesses = scenario.expectedProcesses;
        Assertions.assertThat(taskManager.listProcesses(Sorting.BY_TIMESTAMP))
                .containsExactlyElementsOf(expectedProcesses);
    }

    enum AddingScenario {
        SHOULD_ACCEPT_PROCESSES_WHEN_ENOUGH_SPACE(
                2,
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_2),
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_2)
        ),
        SHOULD_IGNORE_PROCESSES_WHEN_NOT_ENOUGH_SPACE(
                2,
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_2, MEDIUM_PROCESS_3),
                List.of(HIGH_PROCESS_4, MEDIUM_PROCESS_2)
        );

        private final int givenSize;
        private final List<Process> givenProcesses;
        private final List<Process> expectedProcesses;

        AddingScenario(int givenSize,
                       List<Process> givenProcesses,
                       List<Process> expectedProcesses) {
            this.givenSize = givenSize;
            this.givenProcesses = givenProcesses;
            this.expectedProcesses = expectedProcesses;
        }
    }
}