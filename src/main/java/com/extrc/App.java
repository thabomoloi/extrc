package com.extrc;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;
import com.extrc.services.RationalClosureImpl;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        PlBeliefSet kb = new PlBeliefSet();
        Proposition p = new Proposition("p");
        Proposition b = new Proposition("b");
        Proposition w = new Proposition("w");
        Proposition f = new Proposition("f");

        kb.add(new Implication(p, b));
        kb.add(new DefeasibleImplication(b, w));
        kb.add(new DefeasibleImplication(b, f));
        kb.add(new DefeasibleImplication(p, new Negation(f)));

        System.out.println(RationalClosureImpl.query(new KnowledgeBase(kb), new DefeasibleImplication(p, f)));
        // new RationalClosureServiceImpl(kb, new DefeasibleImplication(p, f));

        // KnowledgeBase K = new KnowledgeBase(kb);
        // Rank r1 = new Rank(0, K);

        // Rank r2 = new Rank(r1);

        // System.out.println("R2 = R1 ? " + (r1 == r2));
        // System.out.println("R2(K) = R1(K) ? " + (r1.knowledgeBase ==
        // r2.knowledgeBase));

    }
}
