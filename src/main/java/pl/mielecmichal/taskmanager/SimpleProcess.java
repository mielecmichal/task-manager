package pl.mielecmichal.taskmanager;


public record SimpleProcess(long pid, Priority priority) implements Process {

    @Override
    public void kill() {
    }
    
}
