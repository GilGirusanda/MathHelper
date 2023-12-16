package com.yehor.helper.repositories;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.services.servicesImpl.EquationSolverServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RootRepoTest {

    @Autowired
    private EquationRepo equationRepo;
    @Autowired
    private RootRepo rootRepo;

    @Test
    void ItShouldFindAllRootsByRootValue() {
        // given
        String eq = "2*x+5=17";
        Equation savedEquation = equationRepo.save(new Equation(null, eq, null));

        String[] roots = {"6"};
        Double rootParsed = Double.parseDouble(roots[0]);
        Root rootEntity = new Root(null, rootParsed, savedEquation);
        rootRepo.save(rootEntity);

        // when
        List<Root> foundRoots = rootRepo.findAllByRootValue(rootParsed);

        // then
        assertNotEquals(List.of(), foundRoots);
    }

    @Test
    void ItShouldFindOneRootByRootValue() {
        // given
        String eq = "2*x+5=17";
        Equation savedEquation = equationRepo.save(new Equation(null, eq, null));

        String[] roots = {"6"};
        Double rootParsed = Double.parseDouble(roots[0]);
        Root rootEntity = new Root(null, rootParsed, savedEquation);
        rootRepo.save(rootEntity);

        // when
        Root foundRoot = rootRepo.findByRootValue(rootParsed);

        // then
        assertNotNull(foundRoot);
    }
}