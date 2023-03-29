import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBrand } from '../brand.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../brand.test-samples';

import { BrandService } from './brand.service';

const requireRestSample: IBrand = {
  ...sampleWithRequiredData,
};

describe('Brand Service', () => {
  let service: BrandService;
  let httpMock: HttpTestingController;
  let expectedResult: IBrand | IBrand[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BrandService);
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

    it('should create a Brand', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const brand = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(brand).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Brand', () => {
      const brand = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(brand).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Brand', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Brand', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Brand', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBrandToCollectionIfMissing', () => {
      it('should add a Brand to an empty array', () => {
        const brand: IBrand = sampleWithRequiredData;
        expectedResult = service.addBrandToCollectionIfMissing([], brand);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(brand);
      });

      it('should not add a Brand to an array that contains it', () => {
        const brand: IBrand = sampleWithRequiredData;
        const brandCollection: IBrand[] = [
          {
            ...brand,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBrandToCollectionIfMissing(brandCollection, brand);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Brand to an array that doesn't contain it", () => {
        const brand: IBrand = sampleWithRequiredData;
        const brandCollection: IBrand[] = [sampleWithPartialData];
        expectedResult = service.addBrandToCollectionIfMissing(brandCollection, brand);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(brand);
      });

      it('should add only unique Brand to an array', () => {
        const brandArray: IBrand[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const brandCollection: IBrand[] = [sampleWithRequiredData];
        expectedResult = service.addBrandToCollectionIfMissing(brandCollection, ...brandArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const brand: IBrand = sampleWithRequiredData;
        const brand2: IBrand = sampleWithPartialData;
        expectedResult = service.addBrandToCollectionIfMissing([], brand, brand2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(brand);
        expect(expectedResult).toContain(brand2);
      });

      it('should accept null and undefined values', () => {
        const brand: IBrand = sampleWithRequiredData;
        expectedResult = service.addBrandToCollectionIfMissing([], null, brand, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(brand);
      });

      it('should return initial array if no Brand is added', () => {
        const brandCollection: IBrand[] = [sampleWithRequiredData];
        expectedResult = service.addBrandToCollectionIfMissing(brandCollection, undefined, null);
        expect(expectedResult).toEqual(brandCollection);
      });
    });

    describe('compareBrand', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBrand(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBrand(entity1, entity2);
        const compareResult2 = service.compareBrand(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBrand(entity1, entity2);
        const compareResult2 = service.compareBrand(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBrand(entity1, entity2);
        const compareResult2 = service.compareBrand(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
