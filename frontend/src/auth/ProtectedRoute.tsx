import { Navigate } from "react-router";
import { useAuthStore } from "@/store/useAuthStore";

import { ReactNode } from "react";

const ProtectedRoute = ({ children }: { children: ReactNode }) => {
  const accessToken = useAuthStore((state) => state.accessToken);

  if (!accessToken) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
