import { IUser } from 'app/entities/user/user.model';
import { IBrand } from 'app/entities/brand/brand.model';

export interface ICar {
  id: number;
  name?: string | null;
  model?: string | null;
  plate?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  brand?: Pick<IBrand, 'id' | 'name'> | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
