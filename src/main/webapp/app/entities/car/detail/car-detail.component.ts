import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICar } from '../car.model';

@Component({
  selector: 'jhi-car-detail',
  templateUrl: './car-detail.component.html',
})
export class CarDetailComponent implements OnInit {
  car: ICar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      this.car = car;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
