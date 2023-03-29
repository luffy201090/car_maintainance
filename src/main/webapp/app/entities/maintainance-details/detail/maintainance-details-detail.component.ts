import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaintainanceDetails } from '../maintainance-details.model';

@Component({
  selector: 'jhi-maintainance-details-detail',
  templateUrl: './maintainance-details-detail.component.html',
})
export class MaintainanceDetailsDetailComponent implements OnInit {
  maintainanceDetails: IMaintainanceDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintainanceDetails }) => {
      this.maintainanceDetails = maintainanceDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
