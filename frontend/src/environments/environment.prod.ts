// crmUrl must be the CRM's own origin (separate host/port), matching
// SSO_CRM_ORIGIN / the registered SSO redirect URI in auth-service.
// Override per-deployment; defaults below match docker-compose.prod.yml.
export const environment = {
  production: true,
  baseUrl: '/api',
  crmUrl: 'http://localhost:4201',
};
