import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICorporation, NewCorporation } from '../corporation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICorporation for edit and NewCorporationFormGroupInput for create.
 */
type CorporationFormGroupInput = ICorporation | PartialWithRequiredKeyOf<NewCorporation>;

type CorporationFormDefaults = Pick<NewCorporation, 'id'>;

type CorporationFormGroupContent = {
  id: FormControl<ICorporation['id'] | NewCorporation['id']>;
  name: FormControl<ICorporation['name']>;
  description: FormControl<ICorporation['description']>;
};

export type CorporationFormGroup = FormGroup<CorporationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CorporationFormService {
  createCorporationFormGroup(corporation: CorporationFormGroupInput = { id: null }): CorporationFormGroup {
    const corporationRawValue = {
      ...this.getFormDefaults(),
      ...corporation,
    };
    return new FormGroup<CorporationFormGroupContent>({
      id: new FormControl(
        { value: corporationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(corporationRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(corporationRawValue.description),
    });
  }

  getCorporation(form: CorporationFormGroup): ICorporation | NewCorporation {
    return form.getRawValue() as ICorporation | NewCorporation;
  }

  resetForm(form: CorporationFormGroup, corporation: CorporationFormGroupInput): void {
    const corporationRawValue = { ...this.getFormDefaults(), ...corporation };
    form.reset(
      {
        ...corporationRawValue,
        id: { value: corporationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CorporationFormDefaults {
    return {
      id: null,
    };
  }
}
