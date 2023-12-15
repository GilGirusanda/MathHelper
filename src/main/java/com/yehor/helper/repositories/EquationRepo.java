package com.yehor.helper.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yehor.helper.models.Equation;

@Repository
public interface EquationRepo extends JpaRepository<Equation, Long> {

    Equation findByValue(String val);

    // @Query("SELECT e FROM Equation e JOIN Root r ON e.id = r.equationId WHERE
    // r.value IN :rootValues")
    @Query("SELECT e FROM Equation e JOIN e.rootSet r WHERE r.value IN :rootValues")
    List<Equation> findByRoots(@Param("rootValues") List<Double> rootValues);

    // @Query("SELECT e FROM Equation e JOIN Root r ON e.id = r.equationId GROUP BY
    // e.id HAVING COUNT(r) = 1")
    @Query("SELECT e FROM Equation e JOIN e.rootSet r GROUP BY e.id HAVING COUNT(r) = 1")
    List<Equation> findEquationsWithSingleRoot();

}
