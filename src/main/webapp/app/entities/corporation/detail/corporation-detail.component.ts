import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICorporation } from '../corporation.model';

@Component({
  selector: 'jhi-corporation-detail',
  templateUrl: './corporation-detail.component.html',
})
export class CorporationDetailComponent implements OnInit {
  corporation: ICorporation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corporation }) => {
      this.corporation = corporation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
