package pl.mielecmichal.taskmanager.queueing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.mielecmichal.taskmanager.TaskManager;

interface AddingIncorrectTest extends TestedManagerProvider{

    @Test
    default void shouldThrowWhenAddingNull() {
        //given
        TaskManager taskManager = provideTaskManager(10);

        //when
        Throwable throwable = Assertions.catchThrowable(() -> taskManager.addProcess(null));

        //then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    default void shouldThrowWhenAddingDuplicate() {
        //given
        TaskManager taskManager = provideTaskManager(10);
        taskManager.addProcess(Prototypes.MEDIUM_PROCESS_1);

        //when
        Throwable throwable = Assertions.catchThrowable(() -> taskManager.addProcess(Prototypes.MEDIUM_PROCESS_1));

        //then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
