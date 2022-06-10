package az.telegram.bot.model.enums;

public enum QueryType {
    NEXT("NEXT");
    private final String val;

    public String getVal() {
        return val;
    }

    QueryType(String val) {
        this.val = val;
    }
}
