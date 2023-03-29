import { ICorporation } from 'app/entities/corporation/corporation.model';

export interface IBrand {
  id: number;
  name?: string | null;
  corporation?: Pick<ICorporation, 'id' | 'name'> | null;
}

export type NewBrand = Omit<IBrand, 'id'> & { id: null };
