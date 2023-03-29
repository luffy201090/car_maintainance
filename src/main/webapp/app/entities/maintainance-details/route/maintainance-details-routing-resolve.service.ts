import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaintainanceDetails } from '../maintainance-details.model';
import { MaintainanceDetailsService } from '../service/maintainance-details.service';

@Injectable({ providedIn: 'root' })
export class MaintainanceDetailsRoutingResolveService implements Resolve<IMaintainanceDetails | null> {
  constructor(protected service: MaintainanceDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMaintainanceDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((maintainanceDetails: HttpResponse<IMaintainanceDetails>) => {
          if (maintainanceDetails.body) {
            return of(maintainanceDetails.body);
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
