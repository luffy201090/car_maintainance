import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaintainanceDetails, NewMaintainanceDetails } from '../maintainance-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaintainanceDetails for edit and NewMaintainanceDetailsFormGroupInput for create.
 */
type MaintainanceDetailsFormGroupInput = IMaintainanceDetails | PartialWithRequiredKeyOf<NewMaintainanceDetails>;

type MaintainanceDetailsFormDefaults = Pick<NewMaintainanceDetails, 'id'>;

type MaintainanceDetailsFormGroupContent = {
  id: FormControl<IMaintainanceDetails['id'] | NewMaintainanceDetails['id']>;
  name: FormControl<IMaintainanceDetails['name']>;
  action: FormControl<IMaintainanceDetails['action']>;
  price: FormControl<IMaintainanceDetails['price']>;
  maintainance: FormControl<IMaintainanceDetails['maintainance']>;
};

export type MaintainanceDetailsFormGroup = FormGroup<MaintainanceDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaintainanceDetailsFormService {
  createMaintainanceDetailsFormGroup(maintainanceDetails: MaintainanceDetailsFormGroupInput = { id: null }): MaintainanceDetailsFormGroup {
    const maintainanceDetailsRawValue = {
      ...this.getFormDefaults(),
      ...maintainanceDetails,
    };
    return new FormGroup<MaintainanceDetailsFormGroupContent>({
      id: new FormControl(
        { value: maintainanceDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(maintainanceDetailsRawValue.name, {
        validators: [Validators.required],
      }),
      action: new FormControl(maintainanceDetailsRawValue.action, {
        validators: [Validators.required],
      }),
      price: new FormControl(maintainanceDetailsRawValue.price),
      maintainance: new FormControl(maintainanceDetailsRawValue.maintainance, {
        validators: [Validators.required],
      }),
    });
  }

  getMaintainanceDetails(form: MaintainanceDetailsFormGroup): IMaintainanceDetails | NewMaintainanceDetails {
    return form.getRawValue() as IMaintainanceDetails | NewMaintainanceDetails;
  }

  resetForm(form: MaintainanceDetailsFormGroup, maintainanceDetails: MaintainanceDetailsFormGroupInput): void {
    const maintainanceDetailsRawValue = { ...this.getFormDefaults(), ...maintainanceDetails };
    form.reset(
      {
        ...maintainanceDetailsRawValue,
        id: { value: maintainanceDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MaintainanceDetailsFormDefaults {
    return {
      id: null,
    };
  }
}
