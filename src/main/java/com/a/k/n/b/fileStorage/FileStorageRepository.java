package com.a.k.n.b.fileStorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    @Query("select f from file_storage f where f.token = ?1")
    Optional<FileStorage> findByToken(String token);
}
