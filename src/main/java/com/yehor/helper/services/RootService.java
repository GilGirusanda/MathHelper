package com.yehor.helper.services;

import java.util.List;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;

public interface RootService {

    void save(Double value, Equation equationEntity);

    List<Root> findAllByValue(Double root);

    Root findByValue(Double root);

}
