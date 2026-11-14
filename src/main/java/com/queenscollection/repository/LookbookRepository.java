package com.queenscollection.repository;

import com.queenscollection.entity.Lookbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookbookRepository extends JpaRepository<Lookbook, Long> {

    List<Lookbook> findByActiveTrueOrderByDisplayOrderAsc();
}
