import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../maintainance.test-samples';

import { MaintainanceFormService } from './maintainance-form.service';

describe('Maintainance Form Service', () => {
  let service: MaintainanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaintainanceFormService);
  });

  describe('Service methods', () => {
    describe('createMaintainanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaintainanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            level: expect.any(Object),
            price: expect.any(Object),
            place: expect.any(Object),
            date: expect.any(Object),
            car: expect.any(Object),
          })
        );
      });

      it('passing IMaintainance should create a new form with FormGroup', () => {
        const formGroup = service.createMaintainanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            level: expect.any(Object),
            price: expect.any(Object),
            place: expect.any(Object),
            date: expect.any(Object),
            car: expect.any(Object),
          })
        );
      });
    });

    describe('getMaintainance', () => {
      it('should return NewMaintainance for default Maintainance initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMaintainanceFormGroup(sampleWithNewData);

        const maintainance = service.getMaintainance(formGroup) as any;

        expect(maintainance).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaintainance for empty Maintainance initial value', () => {
        const formGroup = service.createMaintainanceFormGroup();

        const maintainance = service.getMaintainance(formGroup) as any;

        expect(maintainance).toMatchObject({});
      });

      it('should return IMaintainance', () => {
        const formGroup = service.createMaintainanceFormGroup(sampleWithRequiredData);

        const maintainance = service.getMaintainance(formGroup) as any;

        expect(maintainance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaintainance should not enable id FormControl', () => {
        const formGroup = service.createMaintainanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaintainance should disable id FormControl', () => {
        const formGroup = service.createMaintainanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
