import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MaintainanceDetailsComponent } from '../list/maintainance-details.component';
import { MaintainanceDetailsDetailComponent } from '../detail/maintainance-details-detail.component';
import { MaintainanceDetailsUpdateComponent } from '../update/maintainance-details-update.component';
import { MaintainanceDetailsRoutingResolveService } from './maintainance-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const maintainanceDetailsRoute: Routes = [
  {
    path: '',
    component: MaintainanceDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaintainanceDetailsDetailComponent,
    resolve: {
      maintainanceDetails: MaintainanceDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaintainanceDetailsUpdateComponent,
    resolve: {
      maintainanceDetails: MaintainanceDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaintainanceDetailsUpdateComponent,
    resolve: {
      maintainanceDetails: MaintainanceDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(maintainanceDetailsRoute)],
  exports: [RouterModule],
})
export class MaintainanceDetailsRoutingModule {}
