package com.yehor.helper.services.servicesImpl;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.repositories.RootRepo;
import com.yehor.helper.services.EquationService;
import com.yehor.helper.services.RootService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RootServiceImplTest {

    @Mock
    private RootRepo rootRepo;
    private RootService rootService;

    @BeforeEach
    void setUp() {
        rootService = new RootServiceImpl(rootRepo);
    }

    @Test
    void save() {
        // given
        Equation equation = new Equation(1L, "2*x+5=17", new HashSet<>());
        Double rootValue = 6d;
        Root root = new Root(1L, rootValue, equation);

        when(rootRepo.save(any(Root.class))).thenReturn(root);

        // when
        Root savedRoot = rootService.save(rootValue, equation);

        // then
        assertNotNull(savedRoot);
    }

    @Test
    void findAllByValue() {
        // given
        Double rootValue = 6.0;
        Equation equation = new Equation(1L, "2*x+5=17", new HashSet<>());
        Root root = new Root(1L, rootValue, equation);

        // Mock the behavior of the rootRepo to return a list of roots by value
        when(rootRepo.findAllByRootValue(rootValue)).thenReturn(List.of(root));

        // when
        List<Root> roots = rootService.findAllByValue(rootValue);

        // then
        assertNotNull(roots);
        assertEquals(1, roots.size());
        assertEquals(rootValue, roots.get(0).getRootValue());
        assertEquals(equation.getId(), roots.get(0).getEquation().getId());
    }

    @Test
    void findByValue() {
        // given
        Double rootValue = 6.0;
        Equation equation = new Equation(1L, "2*x+5=17", new HashSet<>());
        Root root = new Root(1L, rootValue, equation);

        // Mock the behavior of the rootRepo to return a root by value
        when(rootRepo.findByRootValue(rootValue)).thenReturn(root);

        // when
        Root foundRoot = rootService.findByValue(rootValue);

        // then
        assertNotNull(foundRoot);
        assertEquals(rootValue, foundRoot.getRootValue());
        assertEquals(equation.getId(), foundRoot.getEquation().getId());
    }
}