package com.yehor.helper.services;

import java.util.List;

import com.yehor.helper.models.Equation;

public interface EquationService {
    boolean addEquation(String val);

    boolean processEquation(String val, Double root);

    Equation saveEquation(String eq);

    List<Equation> findEquationsWithSingleRoot();

    List<Equation> findEquationsByAnyRootFromArray(String[] roots);

    List<Equation> findEquationsWithMultipleRootsFromArray(String[] roots);

    Equation findEquationByRoot(Double root);

}
