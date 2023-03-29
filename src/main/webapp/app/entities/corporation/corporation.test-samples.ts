import { ICorporation, NewCorporation } from './corporation.model';

export const sampleWithRequiredData: ICorporation = {
  id: 77577,
  name: 'array end-to-end Fresh',
};

export const sampleWithPartialData: ICorporation = {
  id: 9623,
  name: 'Sports Supervisor',
  description: 'reinvent Usability',
};

export const sampleWithFullData: ICorporation = {
  id: 14613,
  name: 'panel bottom-line',
  description: 'portals',
};

export const sampleWithNewData: NewCorporation = {
  name: 'Soft initiative Grocery',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
