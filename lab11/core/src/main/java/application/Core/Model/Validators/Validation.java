package application.Core.Model.Validators;

@FunctionalInterface
public interface Validation<K> {
    ValidatorResult test(K param);

    default Validation<K> and (Validation<K> other) {
        return (param) -> {
            ValidatorResult firstResult = this.test(param);
            return firstResult != null ? firstResult : other.test(param);
        };
    }

    default Validation<K> or (Validation<K> other) {
        return (param) -> {
            ValidatorResult firstResult = this.test(param);
            return firstResult != null ? firstResult : other.test(param);
        };
    }
}
