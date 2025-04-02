import { BrowserRouter, Route, Routes } from "react-router";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ProtectedRoute from "@/auth/ProtectedRoute.tsx";
import List from "./pages/List";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <Auth>
              <List />
            </Auth>
          }
        />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </BrowserRouter>
  );
}

function Auth({ children }: { children: React.ReactNode }) {
  return (
    <ProtectedRoute>
      <main className="h-full w-full">{children}</main>
    </ProtectedRoute>
  );
}

export default App;
