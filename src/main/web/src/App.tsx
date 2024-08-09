// import reactLogo from "./assets/react.svg";
// import viteLogo from "/vite.svg";
import "katex/dist/katex.min.css";
import { Toaster } from "@/components/ui/toaster";
import { MainContent } from "./components/MainContent";

function App() {
  return (
    <div className="bg-background">
      <div className="space-y-4 p-8 pt-6">
        <MainContent />
      </div>
      <Toaster />
    </div>
  );
}

export default App;
