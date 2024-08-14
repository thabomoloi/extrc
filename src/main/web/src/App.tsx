import "katex/dist/katex.min.css";
import { Toaster } from "@/components/ui/toaster";
import { MainContent } from "@/components/MainContent";
import { Footer } from "@/components/Footer";
// import { Header } from "./components/Header";

function App() {
  return (
    <div className="bg-background flex flex-col items-center">
      {/* <Header /> */}
      <div className="space-y-4 p-8 pt-6 max-w-screen-xl w-full">
        <MainContent />
      </div>
      <Footer className="w-full" />
      <Toaster />
    </div>
  );
}

export default App;
