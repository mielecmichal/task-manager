package pl.mielecmichal.taskmanager;

public interface Process {
    long pid();

    Priority priority();

    void kill();

    enum Priority {
        LOW(1),
        MEDIUM(2),
        HIGH(3);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }
}
