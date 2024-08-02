import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function texFormula(formula: string): string {
  return formula
    .replace("~>", " \\vsim ")
    .replace("=>", " \\to ")
    .replace("!", " \\neg ")
    .replace("&&", " \\land ")
    .replace("||", " \\lor ")
    .replace("<=>", " \\leftrightarrow ");
}

export enum Operation {
  View,
  Edit,
  Update,
}
