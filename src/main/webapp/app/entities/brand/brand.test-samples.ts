import { IBrand, NewBrand } from './brand.model';

export const sampleWithRequiredData: IBrand = {
  id: 69581,
  name: 'Human copying experiences',
};

export const sampleWithPartialData: IBrand = {
  id: 68630,
  name: 'mobile Pizza',
};

export const sampleWithFullData: IBrand = {
  id: 53595,
  name: 'Metal Sol empower',
};

export const sampleWithNewData: NewBrand = {
  name: 'primary',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
