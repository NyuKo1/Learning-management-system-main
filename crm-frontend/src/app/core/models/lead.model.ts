export type LeadStatus = 'NEW' | 'CONTACTED' | 'QUALIFIED' | 'PROPOSAL' | 'WON' | 'LOST';
export type LeadSource = 'WEBSITE' | 'REFERRAL' | 'SOCIAL' | 'COLD_CALL' | 'AD' | 'OTHER';

export interface Lead {
  id: number;
  fullName: string;
  phone: string;
  email?: string;
  status: LeadStatus;
  source: LeadSource;
  interestedCourseId?: number;
  interestedCourseTitle?: string;
  notes?: string;
  managerId?: number;
  managerName?: string;
  createdAt: string;
  updatedAt: string;
}

export interface LeadRequest {
  fullName: string;
  phone: string;
  email?: string;
  status: LeadStatus;
  source: LeadSource;
  interestedCourseId?: number;
  notes?: string;
}

export const LEAD_STATUS_LABELS: Record<LeadStatus, string> = {
  NEW:       'Новый',
  CONTACTED: 'Контакт',
  QUALIFIED: 'Квалифицирован',
  PROPOSAL:  'Предложение',
  WON:       'Клиент',
  LOST:      'Отказ'
};

export const LEAD_SOURCE_LABELS: Record<LeadSource, string> = {
  WEBSITE:   'Сайт',
  REFERRAL:  'Реферал',
  SOCIAL:    'Соцсети',
  COLD_CALL: 'Холодный звонок',
  AD:        'Реклама',
  OTHER:     'Другое'
};
