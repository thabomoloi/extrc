import { cn } from "@/lib/utils";
import { Logo } from "./ui/logo";
import { Button } from "./ui/button";

interface HeaderProps {
  className?: string;
}

function Header({ className }: HeaderProps) {
  return (
    <header
      className={cn(
        "text-center flex items-center justify-between p-6 border-b",
        className
      )}
    >
      <div className="flex items-center gap-2">
        <Logo className="w-8" />
      </div>
      <div className="grid grid-cols-2 gap-4">
        <Button>Definitions</Button>
        <Button>Syntax</Button>
      </div>
    </header>
  );
}

export { Header };
