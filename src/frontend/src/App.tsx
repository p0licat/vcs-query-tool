import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import { Layout } from "./features/components/layout/Layout";
import { SearchRepos } from "./features/pages/SearchRepos";
import { Users } from "./features/pages/Users";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Users />} />
          <Route path="search/:username" element={<SearchRepos />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
