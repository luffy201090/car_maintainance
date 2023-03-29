import dayjs from 'dayjs/esm';
import { ICar } from 'app/entities/car/car.model';

export interface IMaintainance {
  id: number;
  level?: string | null;
  price?: number | null;
  place?: string | null;
  date?: dayjs.Dayjs | null;
  car?: Pick<ICar, 'id' | 'name'> | null;
}

export type NewMaintainance = Omit<IMaintainance, 'id'> & { id: null };
