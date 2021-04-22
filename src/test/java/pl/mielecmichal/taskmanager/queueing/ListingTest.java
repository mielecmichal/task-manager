package pl.mielecmichal.taskmanager.queueing;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.TaskManager;

import java.util.Collection;
import java.util.List;

import static pl.mielecmichal.taskmanager.TaskManager.Sorting;
import static pl.mielecmichal.taskmanager.queueing.Prototypes.*;

interface ListingTest extends TestedManagerProvider {

    @ParameterizedTest
    @EnumSource(ListingScenario.class)
    default void shouldListProcesses(ListingScenario scenario) {
        //given
        TaskManager taskManager = provideTaskManager(10);
        scenario.givenProcesses.forEach(taskManager::addProcess);

        //when
        Collection<Process> sorted = taskManager.listProcesses(scenario.givenSorting);

        //then
        Assertions.assertThat(sorted)
                .containsExactlyElementsOf(scenario.expectedProcesses);
    }

    enum ListingScenario {
        SHOULD_LIST_EMPTY(
                List.of(),
                Sorting.BY_PRIORITY,
                List.of()
        ),
        SHOULD_LIST_BY_PID(
                List.of(MEDIUM_PROCESS_3, HIGH_PROCESS_4, MEDIUM_PROCESS_1),
                Sorting.BY_PID,
                List.of(MEDIUM_PROCESS_1, MEDIUM_PROCESS_3, HIGH_PROCESS_4)
        ),
        SHOULD_LIST_BY_PRIORITY(
                List.of(MEDIUM_PROCESS_3, HIGH_PROCESS_4, LOW_PROCESS_6, MEDIUM_PROCESS_1, LOW_PROCESS_5),
                Sorting.BY_PRIORITY,
                List.of(LOW_PROCESS_6, LOW_PROCESS_5, MEDIUM_PROCESS_3, MEDIUM_PROCESS_1, HIGH_PROCESS_4)
        ),
        SHOULD_LIST_BY_TIMESTAMP(
                List.of(MEDIUM_PROCESS_3, HIGH_PROCESS_4, MEDIUM_PROCESS_1),
                Sorting.BY_TIMESTAMP,
                List.of(MEDIUM_PROCESS_3, HIGH_PROCESS_4, MEDIUM_PROCESS_1)
        );
        
        private final List<Process> givenProcesses;
        private final Sorting givenSorting;
        private final List<Process> expectedProcesses;

        ListingScenario(List<Process> givenProcesses,
                        Sorting givenSorting,
                        List<Process> expectedProcesses) {
            this.givenProcesses = givenProcesses;
            this.givenSorting = givenSorting;
            this.expectedProcesses = expectedProcesses;
        }
    }
}