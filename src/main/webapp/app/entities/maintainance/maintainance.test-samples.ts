import dayjs from 'dayjs/esm';

import { IMaintainance, NewMaintainance } from './maintainance.model';

export const sampleWithRequiredData: IMaintainance = {
  id: 56831,
  level: 'Steel local',
  date: dayjs('2023-03-28'),
};

export const sampleWithPartialData: IMaintainance = {
  id: 66915,
  level: 'Books bluetooth quantifying',
  price: 92041,
  place: 'Loan',
  date: dayjs('2023-03-29'),
};

export const sampleWithFullData: IMaintainance = {
  id: 38400,
  level: 'Loan Legacy bandwidth',
  price: 94101,
  place: 'bypassing Soft',
  date: dayjs('2023-03-28'),
};

export const sampleWithNewData: NewMaintainance = {
  level: 'Virginia',
  date: dayjs('2023-03-28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
