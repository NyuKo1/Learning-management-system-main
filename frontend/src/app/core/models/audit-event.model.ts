export interface AuditEvent {
  id: number;
  occurredAt: string;
  requestId: string;
  username?: string;
  userId?: number;
  roles: string[];
  serviceName: string;
  httpMethod: string;
  path: string;
  status: number;
  remoteIp?: string;
  userAgent?: string;
  sensitive: boolean;
  requestBody?: string;
  responseSummary?: string;
  durationMs?: number;
}

export interface AuditPage {
  content: AuditEvent[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface AuditFilters {
  from?: string;
  to?: string;
  username?: string;
  method?: string;
  path?: string;
  status?: number;
  sensitive?: boolean;
}
