import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MaintainanceFormService, MaintainanceFormGroup } from './maintainance-form.service';
import { IMaintainance } from '../maintainance.model';
import { MaintainanceService } from '../service/maintainance.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

@Component({
  selector: 'jhi-maintainance-update',
  templateUrl: './maintainance-update.component.html',
})
export class MaintainanceUpdateComponent implements OnInit {
  isSaving = false;
  maintainance: IMaintainance | null = null;

  carsSharedCollection: ICar[] = [];

  editForm: MaintainanceFormGroup = this.maintainanceFormService.createMaintainanceFormGroup();

  constructor(
    protected maintainanceService: MaintainanceService,
    protected maintainanceFormService: MaintainanceFormService,
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintainance }) => {
      this.maintainance = maintainance;
      if (maintainance) {
        this.updateForm(maintainance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maintainance = this.maintainanceFormService.getMaintainance(this.editForm);
    if (maintainance.id !== null) {
      this.subscribeToSaveResponse(this.maintainanceService.update(maintainance));
    } else {
      this.subscribeToSaveResponse(this.maintainanceService.create(maintainance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaintainance>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(maintainance: IMaintainance): void {
    this.maintainance = maintainance;
    this.maintainanceFormService.resetForm(this.editForm, maintainance);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, maintainance.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.maintainance?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }
}
