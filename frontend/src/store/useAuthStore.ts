import { create } from "zustand";
import {
  getAccessToken,
  setAccessToken,
  setRefreshToken,
  clearTokens as clearTokenStorage,
} from "@/auth/tokenService";

interface AuthStore {
  accessToken: string | null;
  login: (accessToken: string, refreshToken: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthStore>((set) => ({
  accessToken: getAccessToken(),

  login: (accessToken, refreshToken) => {
    setAccessToken(accessToken);
    setRefreshToken(refreshToken);
    set({ accessToken });
  },

  logout: () => {
    clearTokenStorage();
    set({ accessToken: null });
  },
}));
