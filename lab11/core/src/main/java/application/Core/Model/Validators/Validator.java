package application.Core.Model.Validators;

public interface Validator<K> {
    void validate(K entity);
}
