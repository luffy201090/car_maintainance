import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMaintainance } from '../maintainance.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../maintainance.test-samples';

import { MaintainanceService, RestMaintainance } from './maintainance.service';

const requireRestSample: RestMaintainance = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('Maintainance Service', () => {
  let service: MaintainanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaintainance | IMaintainance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MaintainanceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Maintainance', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const maintainance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maintainance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Maintainance', () => {
      const maintainance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maintainance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Maintainance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Maintainance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Maintainance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaintainanceToCollectionIfMissing', () => {
      it('should add a Maintainance to an empty array', () => {
        const maintainance: IMaintainance = sampleWithRequiredData;
        expectedResult = service.addMaintainanceToCollectionIfMissing([], maintainance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintainance);
      });

      it('should not add a Maintainance to an array that contains it', () => {
        const maintainance: IMaintainance = sampleWithRequiredData;
        const maintainanceCollection: IMaintainance[] = [
          {
            ...maintainance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaintainanceToCollectionIfMissing(maintainanceCollection, maintainance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Maintainance to an array that doesn't contain it", () => {
        const maintainance: IMaintainance = sampleWithRequiredData;
        const maintainanceCollection: IMaintainance[] = [sampleWithPartialData];
        expectedResult = service.addMaintainanceToCollectionIfMissing(maintainanceCollection, maintainance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintainance);
      });

      it('should add only unique Maintainance to an array', () => {
        const maintainanceArray: IMaintainance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maintainanceCollection: IMaintainance[] = [sampleWithRequiredData];
        expectedResult = service.addMaintainanceToCollectionIfMissing(maintainanceCollection, ...maintainanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maintainance: IMaintainance = sampleWithRequiredData;
        const maintainance2: IMaintainance = sampleWithPartialData;
        expectedResult = service.addMaintainanceToCollectionIfMissing([], maintainance, maintainance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintainance);
        expect(expectedResult).toContain(maintainance2);
      });

      it('should accept null and undefined values', () => {
        const maintainance: IMaintainance = sampleWithRequiredData;
        expectedResult = service.addMaintainanceToCollectionIfMissing([], null, maintainance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintainance);
      });

      it('should return initial array if no Maintainance is added', () => {
        const maintainanceCollection: IMaintainance[] = [sampleWithRequiredData];
        expectedResult = service.addMaintainanceToCollectionIfMissing(maintainanceCollection, undefined, null);
        expect(expectedResult).toEqual(maintainanceCollection);
      });
    });

    describe('compareMaintainance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaintainance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaintainance(entity1, entity2);
        const compareResult2 = service.compareMaintainance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaintainance(entity1, entity2);
        const compareResult2 = service.compareMaintainance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaintainance(entity1, entity2);
        const compareResult2 = service.compareMaintainance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
