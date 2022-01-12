package util;

public enum ErrorCodes {
autenticacao(1),validacao(2);
	
    private final int value;
    private ErrorCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
