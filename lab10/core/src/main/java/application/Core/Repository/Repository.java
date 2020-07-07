package application.Core.Repository;

import application.Core.Domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface Repository<ID extends Serializable, T extends BaseEntity<ID>>
        extends JpaRepository<T, ID> {
}
