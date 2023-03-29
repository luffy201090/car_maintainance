import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'corporation',
        data: { pageTitle: 'carMaintainanceApp.corporation.home.title' },
        loadChildren: () => import('./corporation/corporation.module').then(m => m.CorporationModule),
      },
      {
        path: 'brand',
        data: { pageTitle: 'carMaintainanceApp.brand.home.title' },
        loadChildren: () => import('./brand/brand.module').then(m => m.BrandModule),
      },
      {
        path: 'car',
        data: { pageTitle: 'carMaintainanceApp.car.home.title' },
        loadChildren: () => import('./car/car.module').then(m => m.CarModule),
      },
      {
        path: 'maintainance',
        data: { pageTitle: 'carMaintainanceApp.maintainance.home.title' },
        loadChildren: () => import('./maintainance/maintainance.module').then(m => m.MaintainanceModule),
      },
      {
        path: 'maintainance-details',
        data: { pageTitle: 'carMaintainanceApp.maintainanceDetails.home.title' },
        loadChildren: () => import('./maintainance-details/maintainance-details.module').then(m => m.MaintainanceDetailsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
