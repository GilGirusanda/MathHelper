package com.yehor.helper.services.servicesImpl;

import com.yehor.helper.models.Equation;
import com.yehor.helper.models.Root;
import com.yehor.helper.repositories.EquationRepo;
import com.yehor.helper.repositories.RootRepo;
import com.yehor.helper.services.EquationService;
import com.yehor.helper.services.RootService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EquationServiceImplTest {

    @Mock
    private EquationRepo equationRepo;
    @Mock
    private RootRepo rootRepo;
    private RootService rootService;
    private EquationService equationService;

    @BeforeEach
    void setUp() {
        rootService = new RootServiceImpl(rootRepo);
        equationService = new EquationServiceImpl(equationRepo, rootService);
    }

    @Test
    void addCorrectEquation() {
        // given
        String val = "2*x+5=17";
        Equation equation = new Equation(1L, val, new HashSet<>());

        // Mock behavior for saving an Equation
        when(equationRepo.save(any(Equation.class))).thenReturn(equation);

        // Mock behavior for findByEquationValue
        when(equationRepo.findByEquationValue(val)).thenReturn(equation);

        // when
        boolean isSuccess = equationService.addEquation(val);
        Equation foundEquation = equationRepo.findByEquationValue(val);

        // then
        assertTrue(isSuccess && !Objects.isNull(foundEquation));
    }

    @Test
    void addIncorrectEquation() {
        // given
        String val = "2**x+5=17";

        // when
        boolean isSuccess = equationService.addEquation(val);

        // then
        assertFalse(isSuccess);
    }

    @Test
    void processEquation() {
        // given
        String val = "2*x+5=17";
        Double rootValue = 6d;
        Equation equation = new Equation(1L, val, new HashSet<>());
        Root root = new Root(1L, rootValue, equation);
        equation.getRootSet().add(root);

        // Mock behavior for saving a Root
        when(rootRepo.save(any(Root.class))).thenReturn(root);

        // Mock behavior for findByEquationValue
        when(equationRepo.findByEquationValue(val)).thenReturn(equation);

        // when
        boolean isSuccess = equationService.processEquation(val, rootValue);

        // then
        assertTrue(isSuccess);
    }

    @Test
    void saveEquation() {
        // given
        String val = "2*x+5=17";
        Equation equation = new Equation(1L, val, new HashSet<>());

        // Mock behavior for saving an Equation
        when(equationRepo.save(any(Equation.class))).thenReturn(equation);

        // when
        Equation savedEquation = equationService.saveEquation(val);

        // then
        assertNotNull(savedEquation);
        assertEquals(val, savedEquation.getEquationValue());
    }

    @Test
    void findEquationsWithSingleRoot() {
        // when
        List<Equation> equations = equationService.findEquationsWithSingleRoot();

        // then
        assertNotNull(equations);
    }

    @Test
    void findEquationsWithMultipleRootsFromArray() {
        // given
        String[] roots = {"2.0", "3.0", "4.0", "5.0"};
        List<Double> rootsDouble = Arrays.asList(2.0, 3.0, 4.0, 5.0);

        // Mock the behavior for finding equations with multiple roots
        Equation equation1 = new Equation();
        Equation equation2 = new Equation();

        Set<Root> rootsSet1 = new HashSet<>(Arrays.asList(new Root(1L, 2.0, equation1), new Root(2L, 3.0, equation1)));
        Set<Root> rootsSet2 = new HashSet<>(Collections.singletonList(new Root(3L, 4.0, equation2)));

        equation1.setRootSet(rootsSet1);
        equation2.setRootSet(rootsSet2);

        List<Equation> equationsWithMultipleRoots = List.of(equation1);

        when(equationRepo.findByRootsStrict(eq(rootsDouble), anyLong())).thenReturn(equationsWithMultipleRoots);

        // when
        List<Equation> equations = equationService.findEquationsWithMultipleRootsFromArray(roots);

        // then
        assertNotNull(equations);
    }

    @Test
    void findEquationsByAnyRootFromArray() {
        // given
        String[] roots = {"2.0", "3.0", "6.0"};
        String val = "2*x+5=17";
        Equation equation = new Equation(1L, val, new HashSet<>());

        // Link root 6.0 to the equation
        Root root = new Root(1L, 6.0, equation);
        equation.getRootSet().add(root);

        // Mock behavior for finding equations by any root from an array
        when(equationRepo.findByRoots(new ArrayList<>(List.of(2.0, 3.0, 6.0)))).thenReturn(List.of(equation));

        // when
        List<Equation> equations = equationService.findEquationsByAnyRootFromArray(roots);

        // then
        assertNotNull(equations);
        assertFalse(equations.isEmpty());
        assertTrue(equations.contains(equation));
    }

    @Test
    void findEquationByRoot() {
        // given
        String val = "2*x+5=17";
        Double rootValue = 6d;
        Equation equation = new Equation(1L, val, new HashSet<>());
        Root root = new Root(1L, rootValue, equation);
        equation.getRootSet().add(root);

        // Mock behavior for finding equation by root
        when(rootService.findByValue(any(Double.class))).thenReturn(new Root(1L, rootValue, equation));

        // when
        Equation foundEquation = equationService.findEquationByRoot(rootValue);

        // then
        assertNotNull(foundEquation);
        // Check if the equation retrieved matches the expected value
        assertEquals(val, foundEquation.getEquationValue());
    }
}