package application.Domain.Validators;

public class ValidatorResult {
    boolean isError;
    String errorMessage;
    ValidatorResult(boolean result, String errorMessage) {this.isError = result; this.errorMessage = errorMessage;}

    public static ValidatorResult ok() {
        return new ValidatorResult(false, "");
    }

    public static ValidatorResult fail(String errorMessage) {
        return new ValidatorResult(true, errorMessage);
    }

    public void throwIfInvalid() { if(isError) throw new ValidatorException(errorMessage); }
}
