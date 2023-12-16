package com.yehor.helper.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yehor.helper.models.Root;

@Repository
public interface RootRepo extends JpaRepository<Root, Long> {

    List<Root> findAllByRootValue(Double value);

    Root findByRootValue(Double value);

}
