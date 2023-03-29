import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BrandComponent } from './list/brand.component';
import { BrandDetailComponent } from './detail/brand-detail.component';
import { BrandUpdateComponent } from './update/brand-update.component';
import { BrandDeleteDialogComponent } from './delete/brand-delete-dialog.component';
import { BrandRoutingModule } from './route/brand-routing.module';

@NgModule({
  imports: [SharedModule, BrandRoutingModule],
  declarations: [BrandComponent, BrandDetailComponent, BrandUpdateComponent, BrandDeleteDialogComponent],
})
export class BrandModule {}
