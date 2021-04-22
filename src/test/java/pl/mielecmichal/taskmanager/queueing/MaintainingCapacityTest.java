package pl.mielecmichal.taskmanager.queueing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.mielecmichal.taskmanager.TaskManager;

import java.util.List;

import static pl.mielecmichal.taskmanager.Process.Priority;
import static pl.mielecmichal.taskmanager.queueing.Prototypes.*;

interface MaintainingCapacityTest extends TestedManagerProvider{

    @Test
    default void shouldMaintainCapacity() {
        //given
        TaskManager givenTaskManager = provideTaskManager(3);
        givenTaskManager.addProcess(MEDIUM_PROCESS_1);
        givenTaskManager.addProcess(LOW_PROCESS_5);
        givenTaskManager.addProcess(LOW_PROCESS_6);

        //when
        givenTaskManager.killGroup(Priority.LOW);
        givenTaskManager.addProcess(HIGH_PROCESS_4);
        givenTaskManager.addProcess(MEDIUM_PROCESS_3);

        //then
        Assertions.assertThat(givenTaskManager.listProcesses(TaskManager.Sorting.BY_TIMESTAMP))
                .containsExactlyElementsOf(List.of(MEDIUM_PROCESS_1, HIGH_PROCESS_4, MEDIUM_PROCESS_3));
    }
}
