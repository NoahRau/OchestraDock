import axios, { AxiosResponse } from "axios";

export function getAccessToken(): string | null {
  return localStorage.getItem("accessToken");
}

export function setAccessToken(token: string): void {
  localStorage.setItem("accessToken", token);
}

export function getRefreshToken(): string | null {
  return localStorage.getItem("refreshToken");
}

export function setRefreshToken(token: string): void {
  localStorage.setItem("refreshToken", token);
}

export function clearTokens(): void {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
}

export function refreshAccessToken(): Promise<
  AxiosResponse<{ accessToken: string }>
> {
  return axios.post("/api/auth/refresh", {
    refreshToken: getRefreshToken(),
  });
}
