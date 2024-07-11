package com.extrc;

import java.io.IOException;

import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.services.RationalClosureServiceImpl;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        PlBeliefSet kb = new PlBeliefSet();
        Proposition p = new Proposition("p");
        Proposition b = new Proposition("b");
        Proposition w = new Proposition("w");
        Proposition f = new Proposition("f");

        kb.add(new Implication(p, b));
        kb.add(new DefeasibleImplication(b, w));
        kb.add(new DefeasibleImplication(b, f));
        kb.add(new DefeasibleImplication(p, new Negation(f)));
        System.out.println("K " + kb);
        new RationalClosureServiceImpl(kb, new DefeasibleImplication(p, f));
    }
}
