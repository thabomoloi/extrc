package com.extrc.draft.models;

import java.util.HashSet;
import java.util.Set;

import org.tweetyproject.commons.util.Pair;
import org.tweetyproject.logics.pl.semantics.PossibleWorld;
import org.tweetyproject.logics.pl.syntax.Conjunction;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.PlPredicate;
import org.tweetyproject.logics.pl.syntax.PlSignature;
import org.tweetyproject.logics.pl.syntax.Proposition;
import org.tweetyproject.logics.pl.syntax.Tautology;

import com.extrc.draft.utils.Symbols;

public class DefeasibleImplication extends PlFormula {
  private Pair<PlFormula, PlFormula> formulas;

  public DefeasibleImplication(PlFormula a, PlFormula b) {
    this.formulas = new Pair<>(a, b);
  }

  public DefeasibleImplication(Pair<PlFormula, PlFormula> formulas) {
    this.formulas = formulas;
  }

  public Pair<PlFormula, PlFormula> getFormulas() {
    return formulas;
  }

  public void setFormulas(Pair<PlFormula, PlFormula> formulas) {
    this.formulas = formulas;
  }

  public void setFormulas(PlFormula left, PlFormula right) {
    this.formulas = new Pair<>(left, right);
  }

  public void setFirstFormula(PlFormula left) {
    this.formulas.setFirst(left);
  }

  public void setSecondFormula(PlFormula right) {
    this.formulas.setSecond(right);
  }

  public PlFormula getFirstFormula() {
    return this.formulas.getFirst();
  }

  public PlFormula getSecondFormula() {
    return this.formulas.getSecond();
  }

  @Override
  public Set<Proposition> getAtoms() {
    Set<Proposition> result = new HashSet<Proposition>();
    result.addAll(formulas.getFirst().getAtoms());
    result.addAll(formulas.getSecond().getAtoms());
    return result;
  }

  @Override
  public Set<PlFormula> getLiterals() {
    Set<PlFormula> result = new HashSet<PlFormula>();
    result.addAll(formulas.getFirst().getLiterals());
    result.addAll(formulas.getSecond().getLiterals());
    return result;
  }

  @Override
  public PlFormula collapseAssociativeFormulas() {
    PlFormula first = this.formulas.getFirst().collapseAssociativeFormulas();
    PlFormula second = this.formulas.getSecond().collapseAssociativeFormulas();
    return new DefeasibleImplication(first, second);
  }

  @Override
  public Set<PlPredicate> getPredicates() {
    Set<PlPredicate> predicates = new HashSet<PlPredicate>();
    predicates.addAll(this.formulas.getFirst().getPredicates());
    predicates.addAll(this.formulas.getSecond().getPredicates());
    return predicates;
  }

  @Override
  public PlFormula trim() {
    PlFormula f1 = formulas.getFirst().trim();
    PlFormula f2 = formulas.getSecond().trim();
    if (f1.equals(f2))
      return new Tautology();
    return new DefeasibleImplication(f1, f2);
  }

  @Override
  public PlFormula toNnf() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Conjunction toCnf() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Set<PossibleWorld> getModels(PlSignature sig) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int numberOfOccurrences(Proposition p) {
    int result = 0;
    result += formulas.getFirst().numberOfOccurrences(p);
    result += formulas.getSecond().numberOfOccurrences(p);
    return result;
  }

  @Override
  public PlFormula replace(Proposition p, PlFormula f, int i) {
    DefeasibleImplication n = this.clone();
    PlFormula left = formulas.getFirst();
    if (left.numberOfOccurrences(p) >= i) {
      n.setFirstFormula(left.replace(p, f, i));
    } else {
      int num = left.numberOfOccurrences(p);
      PlFormula right = formulas.getSecond();
      if (num + right.numberOfOccurrences(p) >= i)
        n.setSecondFormula(right.replace(p, f, i - num));
    }
    return n;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null)
      return false;
    if (getClass() != other.getClass())
      return false;
    DefeasibleImplication otherDI = (DefeasibleImplication) other;
    if (formulas == null) {
      if (otherDI.formulas != null)
        return false;
    } else if (!formulas.equals(otherDI.formulas))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((formulas == null) ? 0 : formulas.hashCode());
    return result;
  }

  @Override
  public DefeasibleImplication clone() {
    return new DefeasibleImplication(this.formulas.getFirst().clone(), this.formulas.getSecond().clone());
  }

  @Override
  public String toString() {
    return "(" + this.formulas.getFirst().toString() + Symbols.DEFEASIBLE_IMPLICATION()
        + this.formulas.getSecond().toString() + ")";
  }

}
