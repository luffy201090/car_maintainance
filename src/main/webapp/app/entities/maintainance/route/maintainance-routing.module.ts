import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MaintainanceComponent } from '../list/maintainance.component';
import { MaintainanceDetailComponent } from '../detail/maintainance-detail.component';
import { MaintainanceUpdateComponent } from '../update/maintainance-update.component';
import { MaintainanceRoutingResolveService } from './maintainance-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const maintainanceRoute: Routes = [
  {
    path: '',
    component: MaintainanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaintainanceDetailComponent,
    resolve: {
      maintainance: MaintainanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaintainanceUpdateComponent,
    resolve: {
      maintainance: MaintainanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaintainanceUpdateComponent,
    resolve: {
      maintainance: MaintainanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(maintainanceRoute)],
  exports: [RouterModule],
})
export class MaintainanceRoutingModule {}
