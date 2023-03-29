import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../maintainance-details.test-samples';

import { MaintainanceDetailsFormService } from './maintainance-details-form.service';

describe('MaintainanceDetails Form Service', () => {
  let service: MaintainanceDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaintainanceDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createMaintainanceDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaintainanceDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            action: expect.any(Object),
            price: expect.any(Object),
            maintainance: expect.any(Object),
          })
        );
      });

      it('passing IMaintainanceDetails should create a new form with FormGroup', () => {
        const formGroup = service.createMaintainanceDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            action: expect.any(Object),
            price: expect.any(Object),
            maintainance: expect.any(Object),
          })
        );
      });
    });

    describe('getMaintainanceDetails', () => {
      it('should return NewMaintainanceDetails for default MaintainanceDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMaintainanceDetailsFormGroup(sampleWithNewData);

        const maintainanceDetails = service.getMaintainanceDetails(formGroup) as any;

        expect(maintainanceDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaintainanceDetails for empty MaintainanceDetails initial value', () => {
        const formGroup = service.createMaintainanceDetailsFormGroup();

        const maintainanceDetails = service.getMaintainanceDetails(formGroup) as any;

        expect(maintainanceDetails).toMatchObject({});
      });

      it('should return IMaintainanceDetails', () => {
        const formGroup = service.createMaintainanceDetailsFormGroup(sampleWithRequiredData);

        const maintainanceDetails = service.getMaintainanceDetails(formGroup) as any;

        expect(maintainanceDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaintainanceDetails should not enable id FormControl', () => {
        const formGroup = service.createMaintainanceDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaintainanceDetails should disable id FormControl', () => {
        const formGroup = service.createMaintainanceDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
