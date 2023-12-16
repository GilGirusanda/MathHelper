package com.yehor.helper.repositories;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.services.servicesImpl.EquationSolverServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EquationRepoTest {

    @Autowired
    private EquationRepo equationRepo;
    @Autowired
    private RootRepo rootRepo;

    @AfterEach
    void tearDown() {
        equationRepo.deleteAll();
    }

    @Test
    void ItShouldFindEquationByValue() {
        // given
        String eq = "2*x+5=17";
        equationRepo.save(new Equation(null, eq, null));

        // when
        Equation foundEquation = equationRepo.findByEquationValue(eq);

        // then
        assertNotNull(foundEquation);
    }

    @Test
    void ItShouldFindEquationByRoots() {
        // given
        String eq = "2*x+5=17";
        Equation savedEquation = equationRepo.save(new Equation(null, eq, null));

        String[] roots = {"1", "6"};
        Root rootEntity = new Root(null, Double.parseDouble(roots[1]), savedEquation);
        rootRepo.save(rootEntity);

        List<Double> rootsParsed = new LinkedList<>();

        for (String root : roots) {
            if (!EquationSolverServiceImpl.isNumeric(root)) {
                continue;
            }
            rootsParsed.add(Double.parseDouble(root));
        }

        // when
        List<Equation> foundEquations = equationRepo.findByRoots(rootsParsed);

        // then
        assertNotEquals(List.of(), foundEquations);
    }

    @Test
    void ItShouldFindEquationsWithSingleRoot() {
        // given
        String eq = "2*x+5=17";
        Equation savedEquation = equationRepo.save(new Equation(null, eq, null));

        String[] roots = {"1", "6"};
        Root rootEntity = new Root(null, Double.parseDouble(roots[1]), savedEquation);
        rootRepo.save(rootEntity);

        // when
        List<Equation> foundEquations = equationRepo.findEquationsWithSingleRoot();
        System.out.println(foundEquations);

        // then
        assertNotEquals(List.of(), foundEquations);
    }

    @Test
    void ItShouldFindEquationsByRootsStrict() {
        // given
        String eq = "2*x+5=17";
        Equation savedEquation = equationRepo.save(new Equation(null, eq, null));

        String[] roots = {"1", "6"};
        Root rootEntity = new Root(null, Double.parseDouble(roots[1]), savedEquation);
        rootRepo.save(rootEntity);

        List<Double> rootsParsed = new LinkedList<>();

        for (String root : roots) {
            if (!EquationSolverServiceImpl.isNumeric(root)) {
                continue;
            }
            rootsParsed.add(Double.parseDouble(root));
        }

        // when
        List<Equation> foundEquations = equationRepo.findByRootsStrict(rootsParsed, rootsParsed.size());

        // then
        assertEquals(List.of(), foundEquations);
    }
}