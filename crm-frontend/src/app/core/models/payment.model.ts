export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';
export type PaymentMethod = 'CARD' | 'CASH' | 'TRANSFER' | 'ONLINE';

export interface Payment {
  id: number;
  clientId: number;
  clientName: string;
  courseId: number;
  courseTitle: string;
  amount: number;
  currency: string;
  status: PaymentStatus;
  method: PaymentMethod;
  transactionId?: string;
  createdAt: string;
}

export const PAYMENT_STATUS_LABELS: Record<PaymentStatus, string> = {
  PENDING:  'В обработке',
  SUCCESS:  'Оплачено',
  FAILED:   'Ошибка',
  REFUNDED: 'Возврат'
};
