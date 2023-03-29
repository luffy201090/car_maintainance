import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBrand } from '../brand.model';
import { BrandService } from '../service/brand.service';

@Injectable({ providedIn: 'root' })
export class BrandRoutingResolveService implements Resolve<IBrand | null> {
  constructor(protected service: BrandService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBrand | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((brand: HttpResponse<IBrand>) => {
          if (brand.body) {
            return of(brand.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
