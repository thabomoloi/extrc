package com.extrc;

import java.io.IOException;

import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

import com.extrc.models_draft.BaseRankModel;
import com.extrc.models_draft.Explanation;
import com.extrc.models_draft.RationalClosure;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        PlParser parser = new PlParser();
        PlBeliefSet cKb = new PlBeliefSet();
        cKb.add(parser.parseFormula("p => b"));
        PlBeliefSet dKb = new PlBeliefSet();
        dKb.add(parser.parseFormula("b=>f"),
                parser.parseFormula("b=>w"),
                parser.parseFormula("p=>!f"));
        new RationalClosure(dKb, parser.parseFormula("p=>!f"), cKb);
    }
}
