package com.extrc;

import org.jruby.RubyInstanceConfig;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;
import com.extrc.services.ExplanationsImpl;
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

        ExplanationsImpl explanationImpl = new ExplanationsImpl();

        RationalClosureImpl.query(new KnowledgeBase(kb), new DefeasibleImplication(p, f), explanationImpl);

        System.out.println(explanationImpl);
        // Create scripting container with optimization configuration
        // ScriptingContainer container = new
        // ScriptingContainer(LocalContextScope.SINGLETHREAD);
        // container.setCompileMode(RubyInstanceConfig.CompileMode.JIT);
        // container.setJitThreshold(50); // Lower threshold for JIT compilation

        // // Execute Ruby script
        // container.runScriptlet(App.class.getResourceAsStream("/main.rb"), "main.rb");
    }
}
