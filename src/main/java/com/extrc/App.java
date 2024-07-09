package com.extrc;

import java.io.IOException;

import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

import com.extrc.models.BaseRankModel;
import com.extrc.models.Explanation;

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
        Explanation exp = new Explanation("base rank");
        BaseRankModel baseRank = new BaseRankModel(dKb, cKb, exp);
        System.out.println(baseRank.rank().toString());
    }
}
