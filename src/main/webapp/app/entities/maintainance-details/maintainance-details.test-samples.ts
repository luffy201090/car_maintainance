import { Action } from 'app/entities/enumerations/action.model';

import { IMaintainanceDetails, NewMaintainanceDetails } from './maintainance-details.model';

export const sampleWithRequiredData: IMaintainanceDetails = {
  id: 92767,
  name: 'Soap AI Factors',
  action: Action['M'],
};

export const sampleWithPartialData: IMaintainanceDetails = {
  id: 65459,
  name: 'Bedfordshire National installation',
  action: Action['A'],
  price: 22943,
};

export const sampleWithFullData: IMaintainanceDetails = {
  id: 85879,
  name: 'Overpass',
  action: Action['A'],
  price: 71007,
};

export const sampleWithNewData: NewMaintainanceDetails = {
  name: 'Metal Plastic',
  action: Action['I'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
