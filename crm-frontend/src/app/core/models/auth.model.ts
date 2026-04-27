export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
}

export interface TokenPayload {
  sub: string;
  roles: string[];
  exp: number;
  iat: number;
  hasLms: boolean;
  hasCrm: boolean;
}
