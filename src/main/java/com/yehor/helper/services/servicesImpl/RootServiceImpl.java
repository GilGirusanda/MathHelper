package com.yehor.helper.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.repositories.RootRepo;
import com.yehor.helper.services.RootService;

@Service
public class RootServiceImpl implements RootService {

    @Autowired
    RootRepo rootRepo;

    @Override
    public void save(Double value, Equation equationEntity) {
        Root rootEntity = new Root(null, value, equationEntity);
        rootRepo.save(rootEntity);
    }

    @Override
    public List<Root> findAllByValue(Double root) {
        return rootRepo.findAllByValue(root);
    }

    @Override
    public Root findByValue(Double root) {
        return rootRepo.findByValue(root);
    }

}
