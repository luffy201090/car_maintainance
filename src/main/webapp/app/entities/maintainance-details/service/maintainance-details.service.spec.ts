import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMaintainanceDetails } from '../maintainance-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../maintainance-details.test-samples';

import { MaintainanceDetailsService } from './maintainance-details.service';

const requireRestSample: IMaintainanceDetails = {
  ...sampleWithRequiredData,
};

describe('MaintainanceDetails Service', () => {
  let service: MaintainanceDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaintainanceDetails | IMaintainanceDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MaintainanceDetailsService);
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

    it('should create a MaintainanceDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const maintainanceDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maintainanceDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaintainanceDetails', () => {
      const maintainanceDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maintainanceDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaintainanceDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaintainanceDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaintainanceDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaintainanceDetailsToCollectionIfMissing', () => {
      it('should add a MaintainanceDetails to an empty array', () => {
        const maintainanceDetails: IMaintainanceDetails = sampleWithRequiredData;
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing([], maintainanceDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintainanceDetails);
      });

      it('should not add a MaintainanceDetails to an array that contains it', () => {
        const maintainanceDetails: IMaintainanceDetails = sampleWithRequiredData;
        const maintainanceDetailsCollection: IMaintainanceDetails[] = [
          {
            ...maintainanceDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing(maintainanceDetailsCollection, maintainanceDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaintainanceDetails to an array that doesn't contain it", () => {
        const maintainanceDetails: IMaintainanceDetails = sampleWithRequiredData;
        const maintainanceDetailsCollection: IMaintainanceDetails[] = [sampleWithPartialData];
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing(maintainanceDetailsCollection, maintainanceDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintainanceDetails);
      });

      it('should add only unique MaintainanceDetails to an array', () => {
        const maintainanceDetailsArray: IMaintainanceDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maintainanceDetailsCollection: IMaintainanceDetails[] = [sampleWithRequiredData];
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing(maintainanceDetailsCollection, ...maintainanceDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maintainanceDetails: IMaintainanceDetails = sampleWithRequiredData;
        const maintainanceDetails2: IMaintainanceDetails = sampleWithPartialData;
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing([], maintainanceDetails, maintainanceDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintainanceDetails);
        expect(expectedResult).toContain(maintainanceDetails2);
      });

      it('should accept null and undefined values', () => {
        const maintainanceDetails: IMaintainanceDetails = sampleWithRequiredData;
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing([], null, maintainanceDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintainanceDetails);
      });

      it('should return initial array if no MaintainanceDetails is added', () => {
        const maintainanceDetailsCollection: IMaintainanceDetails[] = [sampleWithRequiredData];
        expectedResult = service.addMaintainanceDetailsToCollectionIfMissing(maintainanceDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(maintainanceDetailsCollection);
      });
    });

    describe('compareMaintainanceDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaintainanceDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaintainanceDetails(entity1, entity2);
        const compareResult2 = service.compareMaintainanceDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaintainanceDetails(entity1, entity2);
        const compareResult2 = service.compareMaintainanceDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaintainanceDetails(entity1, entity2);
        const compareResult2 = service.compareMaintainanceDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
