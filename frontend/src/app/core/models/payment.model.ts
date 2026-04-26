import { CourseCatalog } from './course-catalog.model';

export interface PaymentRequest {
  courseId: number;
  customerName: string;
  email: string;
  cardNumber: string;
  amount: number;
  currency: string;
}

export interface PaymentResponse {
  id?: number;
  courseId: number;
  customerName: string;
  email: string;
  cardLastFour: string;
  amount: number;
  currency: string;
  status: string;
  userId: string;
  createdAt?: string;
  course?: CourseCatalog;
}
