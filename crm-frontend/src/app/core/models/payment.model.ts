export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
export type PaymentMethod = 'CARD' | 'CASH' | 'TRANSFER' | 'ONLINE';

export interface Payment {
  id: number;
  courseId?: number;
  customerName: string;
  email?: string;
  cardLastFour?: string;
  amount: number;
  currency: string;
  status: PaymentStatus;
  userId?: string;
  method?: PaymentMethod;
  courseTitle?: string;
  clientId?: number;
  course?: { id: number; title: string; price: number };
  createdAt: string;
}

export interface PaymentRequest {
  courseId?: number;
  customerName: string;
  email?: string;
  cardLastFour?: string;
  amount: number;
  currency?: string;
  method?: PaymentMethod;
  clientId?: number;
  courseTitle?: string;
}

export const PAYMENT_STATUS_LABELS: Record<string, string> = {
  PENDING:   'В обработке',
  SUCCESS:   'Оплачено',
  COMPLETED: 'Оплачено',
  FAILED:    'Ошибка',
  REFUNDED:  'Возврат'
};

export const PAYMENT_METHOD_LABELS: Record<PaymentMethod, string> = {
  CARD:     'Банковская карта',
  CASH:     'Наличные',
  TRANSFER: 'Перевод',
  ONLINE:   'Онлайн-оплата'
};
