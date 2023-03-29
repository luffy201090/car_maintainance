import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CorporationComponent } from '../list/corporation.component';
import { CorporationDetailComponent } from '../detail/corporation-detail.component';
import { CorporationUpdateComponent } from '../update/corporation-update.component';
import { CorporationRoutingResolveService } from './corporation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const corporationRoute: Routes = [
  {
    path: '',
    component: CorporationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CorporationDetailComponent,
    resolve: {
      corporation: CorporationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CorporationUpdateComponent,
    resolve: {
      corporation: CorporationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CorporationUpdateComponent,
    resolve: {
      corporation: CorporationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(corporationRoute)],
  exports: [RouterModule],
})
export class CorporationRoutingModule {}
