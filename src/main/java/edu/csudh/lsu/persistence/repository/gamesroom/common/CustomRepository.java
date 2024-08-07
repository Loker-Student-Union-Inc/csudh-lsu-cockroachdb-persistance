package edu.csudh.lsu.persistence.repository.gamesroom.common;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CustomRepository<T, I> extends JpaRepository<T, I> {

    @Transactional
    @Modifying
    List<T> upsertAll(List<T> entities);
}
