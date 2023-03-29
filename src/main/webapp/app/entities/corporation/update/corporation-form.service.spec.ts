import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../corporation.test-samples';

import { CorporationFormService } from './corporation-form.service';

describe('Corporation Form Service', () => {
  let service: CorporationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CorporationFormService);
  });

  describe('Service methods', () => {
    describe('createCorporationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCorporationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing ICorporation should create a new form with FormGroup', () => {
        const formGroup = service.createCorporationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getCorporation', () => {
      it('should return NewCorporation for default Corporation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCorporationFormGroup(sampleWithNewData);

        const corporation = service.getCorporation(formGroup) as any;

        expect(corporation).toMatchObject(sampleWithNewData);
      });

      it('should return NewCorporation for empty Corporation initial value', () => {
        const formGroup = service.createCorporationFormGroup();

        const corporation = service.getCorporation(formGroup) as any;

        expect(corporation).toMatchObject({});
      });

      it('should return ICorporation', () => {
        const formGroup = service.createCorporationFormGroup(sampleWithRequiredData);

        const corporation = service.getCorporation(formGroup) as any;

        expect(corporation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICorporation should not enable id FormControl', () => {
        const formGroup = service.createCorporationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCorporation should disable id FormControl', () => {
        const formGroup = service.createCorporationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
