import { IMaintainance } from 'app/entities/maintainance/maintainance.model';
import { IUser } from 'app/entities/user/user.model';
import { Action } from 'app/entities/enumerations/action.model';

export interface IMaintainanceDetails {
  id: number;
  name?: string | null;
  action?: Action | null;
  price?: number | null;
  maintainance?: Pick<IMaintainance, 'id' | 'level'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewMaintainanceDetails = Omit<IMaintainanceDetails, 'id'> & { id: null };
