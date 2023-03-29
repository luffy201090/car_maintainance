import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaintainance, NewMaintainance } from '../maintainance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaintainance for edit and NewMaintainanceFormGroupInput for create.
 */
type MaintainanceFormGroupInput = IMaintainance | PartialWithRequiredKeyOf<NewMaintainance>;

type MaintainanceFormDefaults = Pick<NewMaintainance, 'id'>;

type MaintainanceFormGroupContent = {
  id: FormControl<IMaintainance['id'] | NewMaintainance['id']>;
  level: FormControl<IMaintainance['level']>;
  price: FormControl<IMaintainance['price']>;
  place: FormControl<IMaintainance['place']>;
  date: FormControl<IMaintainance['date']>;
  car: FormControl<IMaintainance['car']>;
};

export type MaintainanceFormGroup = FormGroup<MaintainanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaintainanceFormService {
  createMaintainanceFormGroup(maintainance: MaintainanceFormGroupInput = { id: null }): MaintainanceFormGroup {
    const maintainanceRawValue = {
      ...this.getFormDefaults(),
      ...maintainance,
    };
    return new FormGroup<MaintainanceFormGroupContent>({
      id: new FormControl(
        { value: maintainanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      level: new FormControl(maintainanceRawValue.level, {
        validators: [Validators.required],
      }),
      price: new FormControl(maintainanceRawValue.price),
      place: new FormControl(maintainanceRawValue.place),
      date: new FormControl(maintainanceRawValue.date, {
        validators: [Validators.required],
      }),
      car: new FormControl(maintainanceRawValue.car, {
        validators: [Validators.required],
      }),
    });
  }

  getMaintainance(form: MaintainanceFormGroup): IMaintainance | NewMaintainance {
    return form.getRawValue() as IMaintainance | NewMaintainance;
  }

  resetForm(form: MaintainanceFormGroup, maintainance: MaintainanceFormGroupInput): void {
    const maintainanceRawValue = { ...this.getFormDefaults(), ...maintainance };
    form.reset(
      {
        ...maintainanceRawValue,
        id: { value: maintainanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MaintainanceFormDefaults {
    return {
      id: null,
    };
  }
}
