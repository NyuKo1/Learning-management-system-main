export interface Client {
  id: number;
  fullName: string;
  phone: string;
  email: string;
  totalPurchases: number;
  totalSpent: number;
  lmsUserId?: number;
  hasLmsAccount: boolean;
  createdAt: string;
}

export interface ClientRequest {
  fullName: string;
  phone: string;
  email: string;
}
