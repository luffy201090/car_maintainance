import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CorporationFormService, CorporationFormGroup } from './corporation-form.service';
import { ICorporation } from '../corporation.model';
import { CorporationService } from '../service/corporation.service';

@Component({
  selector: 'jhi-corporation-update',
  templateUrl: './corporation-update.component.html',
})
export class CorporationUpdateComponent implements OnInit {
  isSaving = false;
  corporation: ICorporation | null = null;

  editForm: CorporationFormGroup = this.corporationFormService.createCorporationFormGroup();

  constructor(
    protected corporationService: CorporationService,
    protected corporationFormService: CorporationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corporation }) => {
      this.corporation = corporation;
      if (corporation) {
        this.updateForm(corporation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const corporation = this.corporationFormService.getCorporation(this.editForm);
    if (corporation.id !== null) {
      this.subscribeToSaveResponse(this.corporationService.update(corporation));
    } else {
      this.subscribeToSaveResponse(this.corporationService.create(corporation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorporation>>): void {
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

  protected updateForm(corporation: ICorporation): void {
    this.corporation = corporation;
    this.corporationFormService.resetForm(this.editForm, corporation);
  }
}
