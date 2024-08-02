// import reactLogo from "./assets/react.svg";
// import viteLogo from "/vite.svg";
import "katex/dist/katex.min.css";
import MainContent from "@/components/MainContent";
import { Toaster } from "@/components/ui/toaster";

function App() {
  return (
    <div className="bg-background">
      <div className="flex-1 space-y-4 p-8 pt-6">
        <MainContent />
      </div>
      <Toaster />
    </div>
  );
}

export default App;
