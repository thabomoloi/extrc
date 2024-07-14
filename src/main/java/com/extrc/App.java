package com.extrc;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.structures.DefeasibleImplication;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.reasoning.ranking.BaseRank;
import com.extrc.reasoning.reasoners.LexicalReasoner;
import com.extrc.reasoning.reasoners.RationalReasoner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        KnowledgeBase kb = new KnowledgeBase();
        Proposition sp = new Proposition("sp");
        Proposition p = new Proposition("p");
        Proposition b = new Proposition("b");
        Proposition w = new Proposition("w");
        Proposition f = new Proposition("f");
        Proposition c = new Proposition("c");

        kb.add(new Implication(sp, p));
        kb.add(new Implication(p, b));
        kb.add(new DefeasibleImplication(b, w));
        kb.add(new DefeasibleImplication(b, f));
        kb.add(new DefeasibleImplication(b, c));
        kb.add(new DefeasibleImplication(sp, f));
        kb.add(new DefeasibleImplication(p, new Negation(f)));

        // ExplanationsImpl explanationImpl = new ExplanationsImpl();

        kb.addAll(kb);
        // System.out.println(LexicographicClosureImpl.query(new KnowledgeBase(kb), new
        // DefeasibleImplication(p, w),
        // explanationImpl));
        DefeasibleReasoner reasoner1 = new RationalReasoner(kb);
        DefeasibleReasoner reasoner2 = new LexicalReasoner(kb);

        System.out.println(reasoner2.query(new DefeasibleImplication(p, w)));
        // Create scripting container with optimization configuration
        // ScriptingContainer container = new
        // ScriptingContainer(LocalContextScope.SINGLETHREAD);
        // container.setCompileMode(RubyInstanceConfig.CompileMode.JIT);
        // container.setJitThreshold(50); // Lower threshold for JIT compilation

        // // Execute Ruby script
        // container.runScriptlet(App.class.getResourceAsStream("/main.rb"), "main.rb");
    }
}
