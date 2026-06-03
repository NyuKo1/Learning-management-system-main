export interface Branding {
  name: string;
  productName: string;
  primaryColor: string;
  logoUrl: string;
  faviconUrl: string;
  supportEmail: string;
}

export const DEFAULT_BRANDING: Branding = {
  name: 'SEC',
  productName: 'SmartEduControl',
  primaryColor: '#0ea5e9',
  logoUrl: '/assets/logo.png',
  faviconUrl: '/assets/favicon.ico',
  supportEmail: 'support@sec.kz',
};
