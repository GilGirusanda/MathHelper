package com.yehor.helper.services.servicesImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.repositories.EquationRepo;
import com.yehor.helper.services.EquationService;
import com.yehor.helper.services.RootService;

import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class EquationServiceImpl implements EquationService {

    @Autowired
    private final EquationRepo equationRepo;

    @Autowired
    private final RootService rootService;

    @Override
    public boolean addEquation(String val) {
        log.info("User input for Equation: {}\n", val);

        if (!EquationSolverServiceImpl.preValidate(val)) {
            log.info("Equation was not saved (failed validation)\n");
            return false;
        }

        if(Objects.isNull(saveEquation(val))) {
            log.info("Equation was not saved (after success validation)\n");
            return false;
        }

        log.info("Equation Saved\n");
        return true;
    }

    @Override
    public boolean processEquation(String val, Double root) {
        // Check if the root corresponds to the equation solution
        if (!EquationSolverServiceImpl.solve(val, root)) {
            log.info("Is not a valid root\n");
            return false;
        }

        // Check is such an equatiom exists in database
        Optional<Equation> equationEntityOpt = Optional.ofNullable(equationRepo.findByEquationValue(val));

        if (!equationEntityOpt.isPresent()) {
            log.info("Can't find such an equation in database. Try to save it...\n");
            if (!addEquation(val)) {
                log.info("Can't save an equation\n");
                return false;
            }
            equationEntityOpt = Optional.ofNullable(equationRepo.findByEquationValue(val));
        }
        if(!equationEntityOpt.isPresent())
            return false;

        if(Objects.isNull(rootService.save(root, equationEntityOpt.get()))) {
            log.info("Can't save an equation\n");
            return false;
        }

        return true;
    }

    @Override
    public Equation saveEquation(String eq) {
        return equationRepo.save(new Equation(null, eq, null));
    }

    @Override
    public List<Equation> findEquationsWithSingleRoot() {
        return equationRepo.findEquationsWithSingleRoot();
    }

    @Override
    public List<Equation> findEquationsWithMultipleRootsFromArray(String[] roots) {
        if (roots.length < 2)
            return List.of();

        List<Double> rootsParsed = new LinkedList<>();

        for (String root : roots) {
            if (!EquationSolverServiceImpl.isNumeric(root)) {
                log.info("Is not a valid root {}\n", root);
                continue;
            }

            rootsParsed.add(Double.parseDouble(root));
        }

        return equationRepo.findByRootsStrict(rootsParsed, rootsParsed.size());
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
