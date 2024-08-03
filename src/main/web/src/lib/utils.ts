import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function texFormula(formula: string): string {
  return formula
    .replaceAll("~>", " \\vsim ")
    .replaceAll("=>", " \\to ")
    .replaceAll("!", " \\neg ")
    .replaceAll("&&", " \\land ")
    .replaceAll("||", " \\lor ")
    .replaceAll("<=>", " \\leftrightarrow ");
}
