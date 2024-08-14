package com.extrc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.BaseRank;
import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.Ranking;
import com.extrc.models.QueryInput;

public class BaseRankServiceImplTest {

    private BaseRankServiceImpl baseRankService;

    @BeforeEach
    public void setUp() {
        baseRankService = new BaseRankServiceImpl();
    }

    @Test
    public void testConstructBaseRank() {

        // Creating mock formulas
        PlFormula p = new Proposition("p");
        PlFormula b = new Proposition("b");
        PlFormula f = new Proposition("f");
        PlFormula w = new Proposition("w");

        PlFormula pb = new Implication(p, b);
        PlFormula bf = new DefeasibleImplication(b, f);
        PlFormula bw = new DefeasibleImplication(b, w);
        PlFormula pNotF = new DefeasibleImplication(p, new Negation(f));

        KnowledgeBase kb = new KnowledgeBase(Arrays.asList(pb, bf, bw, pNotF));

        QueryInput queryInput = new QueryInput(new DefeasibleImplication(p, f), kb);

        BaseRank baseRank = baseRankService.constructBaseRank(queryInput);

        assertNotNull(baseRank);
        assertNotNull(baseRank.getRanking());
        assertNotNull(baseRank.getSequence());
        assertNotNull(baseRank.getQueryInput());

        // Check the time taken is non-negative
        assertTrue(baseRank.getTimeTaken() >= 0);

        // Check if the sequence and ranking are correctly populated
        Ranking sequence = baseRank.getSequence();
        Ranking ranking = baseRank.getRanking();

        assertEquals(3, sequence.size());
        assertEquals(3, ranking.size());

    }

}
