package application.Core.Domain.Validators;

public interface Validator<K> {
    void validate(K entity);
}