import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBrand } from '../brand.model';

@Component({
  selector: 'jhi-brand-detail',
  templateUrl: './brand-detail.component.html',
})
export class BrandDetailComponent implements OnInit {
  brand: IBrand | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ brand }) => {
      this.brand = brand;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
