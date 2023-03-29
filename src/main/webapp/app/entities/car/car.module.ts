import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CarComponent } from './list/car.component';
import { CarDetailComponent } from './detail/car-detail.component';
import { CarUpdateComponent } from './update/car-update.component';
import { CarDeleteDialogComponent } from './delete/car-delete-dialog.component';
import { CarRoutingModule } from './route/car-routing.module';

@NgModule({
  imports: [SharedModule, CarRoutingModule],
  declarations: [CarComponent, CarDetailComponent, CarUpdateComponent, CarDeleteDialogComponent],
})
export class CarModule {}
