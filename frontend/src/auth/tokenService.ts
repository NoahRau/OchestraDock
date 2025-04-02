import axios, { AxiosResponse } from "axios";

export function getAccessToken(): string | null {
  return localStorage.getItem("accessToken");
}

export function setAccessToken(token: string): void {
  localStorage.setItem("accessToken", token);
}

export function getRefreshToken(): string | null {
  return null; // Kein Refresh-Token mehr nötig
}

export function clearTokens(): void {
  localStorage.removeItem("accessToken");
}

export function refreshAccessToken(): Promise<
  AxiosResponse<{ token: string }>
> {
  return axios.post("/api/auth/refresh");
}
