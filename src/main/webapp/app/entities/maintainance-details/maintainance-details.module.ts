import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MaintainanceDetailsComponent } from './list/maintainance-details.component';
import { MaintainanceDetailsDetailComponent } from './detail/maintainance-details-detail.component';
import { MaintainanceDetailsUpdateComponent } from './update/maintainance-details-update.component';
import { MaintainanceDetailsDeleteDialogComponent } from './delete/maintainance-details-delete-dialog.component';
import { MaintainanceDetailsRoutingModule } from './route/maintainance-details-routing.module';

@NgModule({
  imports: [SharedModule, MaintainanceDetailsRoutingModule],
  declarations: [
    MaintainanceDetailsComponent,
    MaintainanceDetailsDetailComponent,
    MaintainanceDetailsUpdateComponent,
    MaintainanceDetailsDeleteDialogComponent,
  ],
})
export class MaintainanceDetailsModule {}
