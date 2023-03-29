import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 96848,
  name: 'local National Account',
  plate: 'Loan',
};

export const sampleWithPartialData: ICar = {
  id: 30253,
  name: 'web-enabled fuchsia',
  plate: 'Soft payment',
};

export const sampleWithFullData: ICar = {
  id: 18809,
  name: 'payment Configuration',
  model: 'groupware redundant Divide',
  plate: 'Bedfordshire',
};

export const sampleWithNewData: NewCar = {
  name: 'Illinois real-time',
  plate: 'California',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
