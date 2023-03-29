import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CorporationComponent } from './list/corporation.component';
import { CorporationDetailComponent } from './detail/corporation-detail.component';
import { CorporationUpdateComponent } from './update/corporation-update.component';
import { CorporationDeleteDialogComponent } from './delete/corporation-delete-dialog.component';
import { CorporationRoutingModule } from './route/corporation-routing.module';

@NgModule({
  imports: [SharedModule, CorporationRoutingModule],
  declarations: [CorporationComponent, CorporationDetailComponent, CorporationUpdateComponent, CorporationDeleteDialogComponent],
})
export class CorporationModule {}
