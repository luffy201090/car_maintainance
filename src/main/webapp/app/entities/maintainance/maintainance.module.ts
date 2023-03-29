import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MaintainanceComponent } from './list/maintainance.component';
import { MaintainanceDetailComponent } from './detail/maintainance-detail.component';
import { MaintainanceUpdateComponent } from './update/maintainance-update.component';
import { MaintainanceDeleteDialogComponent } from './delete/maintainance-delete-dialog.component';
import { MaintainanceRoutingModule } from './route/maintainance-routing.module';

@NgModule({
  imports: [SharedModule, MaintainanceRoutingModule],
  declarations: [MaintainanceComponent, MaintainanceDetailComponent, MaintainanceUpdateComponent, MaintainanceDeleteDialogComponent],
})
export class MaintainanceModule {}
