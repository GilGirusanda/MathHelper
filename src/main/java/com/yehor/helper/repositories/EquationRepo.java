package com.yehor.helper.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yehor.helper.models.Equation;

@Repository
public interface EquationRepo extends JpaRepository<Equation, Long> {

    Equation findByEquationValue(String val);

    @Query("SELECT e FROM Equation e JOIN e.rootSet r WHERE r.rootValue IN :rootValues")
    List<Equation> findByRoots(@Param("rootValues") List<Double> rootValues);

    @Query("SELECT e FROM Equation e JOIN e.rootSet r GROUP BY e.id HAVING COUNT(r) = 1")
    List<Equation> findEquationsWithSingleRoot();

    @Query("SELECT e FROM Equation e " +
        "WHERE e.id IN (" +
        "   SELECT r.equation.id FROM Root r " +
        "   WHERE r.rootValue IN :rootValues " +
        "   GROUP BY r.equation.id " +
        "   HAVING COUNT(DISTINCT r.rootValue) = :numRoots)")
    List<Equation> findByRootsStrict(@Param("rootValues") List<Double> rootValues, @Param("numRoots") long numRoots);
}
