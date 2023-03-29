import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MaintainanceDetailsFormService, MaintainanceDetailsFormGroup } from './maintainance-details-form.service';
import { IMaintainanceDetails } from '../maintainance-details.model';
import { MaintainanceDetailsService } from '../service/maintainance-details.service';
import { IMaintainance } from 'app/entities/maintainance/maintainance.model';
import { MaintainanceService } from 'app/entities/maintainance/service/maintainance.service';
import { Action } from 'app/entities/enumerations/action.model';

@Component({
  selector: 'jhi-maintainance-details-update',
  templateUrl: './maintainance-details-update.component.html',
})
export class MaintainanceDetailsUpdateComponent implements OnInit {
  isSaving = false;
  maintainanceDetails: IMaintainanceDetails | null = null;
  actionValues = Object.keys(Action);

  maintainancesSharedCollection: IMaintainance[] = [];

  editForm: MaintainanceDetailsFormGroup = this.maintainanceDetailsFormService.createMaintainanceDetailsFormGroup();

  constructor(
    protected maintainanceDetailsService: MaintainanceDetailsService,
    protected maintainanceDetailsFormService: MaintainanceDetailsFormService,
    protected maintainanceService: MaintainanceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMaintainance = (o1: IMaintainance | null, o2: IMaintainance | null): boolean =>
    this.maintainanceService.compareMaintainance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintainanceDetails }) => {
      this.maintainanceDetails = maintainanceDetails;
      if (maintainanceDetails) {
        this.updateForm(maintainanceDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maintainanceDetails = this.maintainanceDetailsFormService.getMaintainanceDetails(this.editForm);
    if (maintainanceDetails.id !== null) {
      this.subscribeToSaveResponse(this.maintainanceDetailsService.update(maintainanceDetails));
    } else {
      this.subscribeToSaveResponse(this.maintainanceDetailsService.create(maintainanceDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaintainanceDetails>>): void {
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

  protected updateForm(maintainanceDetails: IMaintainanceDetails): void {
    this.maintainanceDetails = maintainanceDetails;
    this.maintainanceDetailsFormService.resetForm(this.editForm, maintainanceDetails);

    this.maintainancesSharedCollection = this.maintainanceService.addMaintainanceToCollectionIfMissing<IMaintainance>(
      this.maintainancesSharedCollection,
      maintainanceDetails.maintainance
    );
  }

  protected loadRelationshipsOptions(): void {
    this.maintainanceService
      .query()
      .pipe(map((res: HttpResponse<IMaintainance[]>) => res.body ?? []))
      .pipe(
        map((maintainances: IMaintainance[]) =>
          this.maintainanceService.addMaintainanceToCollectionIfMissing<IMaintainance>(
            maintainances,
            this.maintainanceDetails?.maintainance
          )
        )
      )
      .subscribe((maintainances: IMaintainance[]) => (this.maintainancesSharedCollection = maintainances));
  }
}
