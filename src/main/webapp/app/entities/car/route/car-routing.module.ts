import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarComponent } from '../list/car.component';
import { CarDetailComponent } from '../detail/car-detail.component';
import { CarUpdateComponent } from '../update/car-update.component';
import { CarRoutingResolveService } from './car-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const carRoute: Routes = [
  {
    path: '',
    component: CarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarDetailComponent,
    resolve: {
      car: CarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarUpdateComponent,
    resolve: {
      car: CarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarUpdateComponent,
    resolve: {
      car: CarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(carRoute)],
  exports: [RouterModule],
})
export class CarRoutingModule {}
