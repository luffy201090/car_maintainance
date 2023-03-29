import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CarFormService, CarFormGroup } from './car-form.service';
import { ICar } from '../car.model';
import { CarService } from '../service/car.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html',
})
export class CarUpdateComponent implements OnInit {
  isSaving = false;
  car: ICar | null = null;

  usersSharedCollection: IUser[] = [];
  brandsSharedCollection: IBrand[] = [];

  editForm: CarFormGroup = this.carFormService.createCarFormGroup();

  constructor(
    protected carService: CarService,
    protected carFormService: CarFormService,
    protected userService: UserService,
    protected brandService: BrandService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareBrand = (o1: IBrand | null, o2: IBrand | null): boolean => this.brandService.compareBrand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      this.car = car;
      if (car) {
        this.updateForm(car);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const car = this.carFormService.getCar(this.editForm);
    if (car.id !== null) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>): void {
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

  protected updateForm(car: ICar): void {
    this.car = car;
    this.carFormService.resetForm(this.editForm, car);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, car.user);
    this.brandsSharedCollection = this.brandService.addBrandToCollectionIfMissing<IBrand>(this.brandsSharedCollection, car.brand);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.car?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.brandService
      .query()
      .pipe(map((res: HttpResponse<IBrand[]>) => res.body ?? []))
      .pipe(map((brands: IBrand[]) => this.brandService.addBrandToCollectionIfMissing<IBrand>(brands, this.car?.brand)))
      .subscribe((brands: IBrand[]) => (this.brandsSharedCollection = brands));
  }
}
