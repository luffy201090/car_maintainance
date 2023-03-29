import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaintainance } from '../maintainance.model';

@Component({
  selector: 'jhi-maintainance-detail',
  templateUrl: './maintainance-detail.component.html',
})
export class MaintainanceDetailComponent implements OnInit {
  maintainance: IMaintainance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintainance }) => {
      this.maintainance = maintainance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
