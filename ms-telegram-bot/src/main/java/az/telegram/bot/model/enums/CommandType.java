package az.telegram.bot.model.enums;

public enum CommandType {
    START("/start"),
    STOP("/stop"),
    ERROR("error");
    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public static CommandType valueOfCommand(String command) {
        for (CommandType e : values()) {
            if (e.command.equals(command)) {
                return e;
            }
        }
        return ERROR;
    }
}
