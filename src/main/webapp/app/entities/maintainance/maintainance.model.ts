import dayjs from 'dayjs/esm';
import { ICar } from 'app/entities/car/car.model';
import { IUser } from 'app/entities/user/user.model';

export interface IMaintainance {
  id: number;
  level?: string | null;
  price?: number | null;
  place?: string | null;
  date?: dayjs.Dayjs | null;
  car?: Pick<ICar, 'id' | 'name'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewMaintainance = Omit<IMaintainance, 'id'> & { id: null };
