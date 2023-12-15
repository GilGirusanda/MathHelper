package com.yehor.helper.services.servicesImpl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.repositories.EquationRepo;
import com.yehor.helper.services.EquationService;
import com.yehor.helper.services.RootService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EquationServiceImpl implements EquationService {

    @Autowired
    EquationRepo equationRepo;

    @Autowired
    RootService rootService;

    @Override
    public boolean addEquation(String val) {
        log.info("\nUser input: {}\n", val);

        if (!EquationSolverServiceImpl.preValidate(val)) {
            log.info("Was not saved\n");
            return false;
        }

        log.info("Saved\n");
        saveEquation(val);
        return true;
    }

    @Override
    public boolean processEquation(String val, Double root) {
        // if (!EquationSolverServiceImpl.isNumeric(root)) {
        // log.info("Is not a valid root\n");
        // return false;
        // }

        // Double rootParsed = Double.parseDouble(root);

        // Check if the root corresponds to the equation solution
        if (!EquationSolverServiceImpl.solve(val, root)) {
            log.info("Is not a valid root\n");
            return false;
        }

        // Check is such an equatiom exists in database
        Optional<Equation> equationEntityOpt = Optional.ofNullable(equationRepo.findByValue(val));

        if (!equationEntityOpt.isPresent()) {
            log.info("Can't find such an equation in database\n");
            if (!addEquation(val)) {
                log.info("Can't save an equation\n");
                return false;
            }
            equationEntityOpt = Optional.ofNullable(equationRepo.findByValue(val));
        }

        rootService.save(root, equationEntityOpt.get());
        return true;
    }

    @Override
    public void saveEquation(String eq) {
        Equation eqEntity = new Equation(null, eq, null);
        equationRepo.save(eqEntity);
    }

    @Override
    public List<Equation> findEquationsWithSingleRoot() {
        return equationRepo.findEquationsWithSingleRoot();
    }

    @Override
    public List<Equation> findEquationsByAnyRootFromArray(String[] roots) {
        List<Double> rootsParsed = new LinkedList<>();

        for (String root : roots) {
            if (!EquationSolverServiceImpl.isNumeric(root)) {
                log.info("Is not a valid root {}\n", root);
                continue;
            }

            rootsParsed.add(Double.parseDouble(root));
        }

        return equationRepo.findByRoots(rootsParsed);
    }

    @Override
    public Equation findEquationByRoot(Double root) {

        Optional<Root> rootEntityOpt = Optional.ofNullable(rootService.findByValue(root));
        if (!rootEntityOpt.isPresent())
            return null;

        return rootEntityOpt.get().getEquation();
    }

}
