import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BrandComponent } from '../list/brand.component';
import { BrandDetailComponent } from '../detail/brand-detail.component';
import { BrandUpdateComponent } from '../update/brand-update.component';
import { BrandRoutingResolveService } from './brand-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const brandRoute: Routes = [
  {
    path: '',
    component: BrandComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BrandDetailComponent,
    resolve: {
      brand: BrandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BrandUpdateComponent,
    resolve: {
      brand: BrandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BrandUpdateComponent,
    resolve: {
      brand: BrandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(brandRoute)],
  exports: [RouterModule],
})
export class BrandRoutingModule {}
