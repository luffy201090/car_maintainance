import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICorporation } from '../corporation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../corporation.test-samples';

import { CorporationService } from './corporation.service';

const requireRestSample: ICorporation = {
  ...sampleWithRequiredData,
};

describe('Corporation Service', () => {
  let service: CorporationService;
  let httpMock: HttpTestingController;
  let expectedResult: ICorporation | ICorporation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CorporationService);
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

    it('should create a Corporation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const corporation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(corporation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Corporation', () => {
      const corporation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(corporation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Corporation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Corporation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Corporation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCorporationToCollectionIfMissing', () => {
      it('should add a Corporation to an empty array', () => {
        const corporation: ICorporation = sampleWithRequiredData;
        expectedResult = service.addCorporationToCollectionIfMissing([], corporation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(corporation);
      });

      it('should not add a Corporation to an array that contains it', () => {
        const corporation: ICorporation = sampleWithRequiredData;
        const corporationCollection: ICorporation[] = [
          {
            ...corporation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCorporationToCollectionIfMissing(corporationCollection, corporation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Corporation to an array that doesn't contain it", () => {
        const corporation: ICorporation = sampleWithRequiredData;
        const corporationCollection: ICorporation[] = [sampleWithPartialData];
        expectedResult = service.addCorporationToCollectionIfMissing(corporationCollection, corporation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(corporation);
      });

      it('should add only unique Corporation to an array', () => {
        const corporationArray: ICorporation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const corporationCollection: ICorporation[] = [sampleWithRequiredData];
        expectedResult = service.addCorporationToCollectionIfMissing(corporationCollection, ...corporationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const corporation: ICorporation = sampleWithRequiredData;
        const corporation2: ICorporation = sampleWithPartialData;
        expectedResult = service.addCorporationToCollectionIfMissing([], corporation, corporation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(corporation);
        expect(expectedResult).toContain(corporation2);
      });

      it('should accept null and undefined values', () => {
        const corporation: ICorporation = sampleWithRequiredData;
        expectedResult = service.addCorporationToCollectionIfMissing([], null, corporation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(corporation);
      });

      it('should return initial array if no Corporation is added', () => {
        const corporationCollection: ICorporation[] = [sampleWithRequiredData];
        expectedResult = service.addCorporationToCollectionIfMissing(corporationCollection, undefined, null);
        expect(expectedResult).toEqual(corporationCollection);
      });
    });

    describe('compareCorporation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCorporation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCorporation(entity1, entity2);
        const compareResult2 = service.compareCorporation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCorporation(entity1, entity2);
        const compareResult2 = service.compareCorporation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCorporation(entity1, entity2);
        const compareResult2 = service.compareCorporation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
