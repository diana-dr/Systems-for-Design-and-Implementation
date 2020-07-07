package application.Core.Repository;

import application.Core.Model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@NoRepositoryBean
@Transactional
public interface Repository<ID extends Serializable, T extends BaseEntity<ID>>
        extends JpaRepository<T, ID> {
}
