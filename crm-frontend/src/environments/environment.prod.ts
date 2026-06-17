// lmsUrl / crmUrl / landingUrl must be the real origins of each app (separate
// host/port), matching SSO_LMS_ORIGIN / SSO_CRM_ORIGIN in auth-service.
// crmUrl in particular must equal the registered SSO redirect URI origin so the
// authorize + token-exchange redirect_uri match.
// Override per-deployment; defaults below match docker-compose.prod.yml.
export const environment = {
  production: true,
  apiUrl: '/api',
  lmsUrl: 'http://localhost:4200',
  crmUrl: 'http://localhost:4201',
  landingUrl: 'http://localhost:3000'
};
