import { create } from "zustand";
import {
  getAccessToken,
  setAccessToken,
  clearTokens as clearTokenStorage,
} from "@/auth/tokenService";

interface AuthStore {
  accessToken: string | null;
  login: (accessToken: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthStore>((set) => ({
  accessToken: getAccessToken(),

  login: (accessToken) => {
    setAccessToken(accessToken);
    set({ accessToken });
  },

  logout: () => {
    clearTokenStorage();
    set({ accessToken: null });
  },
}));
